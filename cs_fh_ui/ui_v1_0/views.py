import json
from os import system, environ, path, remove

import pytube
from django.http import HttpResponse
from django.shortcuts import render
from django.views.decorators.csrf import csrf_exempt
from google.cloud import storage

from .core.constants import GCP_BUCKET_NAME, GCP_PROJECT_NAME, \
    GCP_CREDENTIALS_FILE_NAME, LOCAL_YOUTUBE_DOWNLOADS_PATH
from .core.gcp import get_gcp_uri
from .core.os_cmd import get_ffmpeg_cmd, get_pending_video_path
from .core.backend import hit_add_video

MAIN_REPOSITORY_PATH = path.abspath(path.join(__file__, "../../../"))
GCP_CREDENTIALS_FILE_NAME_FULL_PATH = MAIN_REPOSITORY_PATH + GCP_CREDENTIALS_FILE_NAME


def homepage(request):
    print("homepage view requested...")
    context = {}
    return render(request, "ui_v1_0/homepage.html", context)


def videos_add(request):
    print("videos_add requested...")
    context = {}
    return render(request, "ui_v1_0/video_add.html")


def videos_search(request):
    print("videos_search requested...")
    context = {}
    return render(request, "ui_v1_0/video_search.html")


def videos_list(request):
    print("videos_list requested...")
    context = {}
    return render(request, "ui_v1_0/video_list.html")


@csrf_exempt
def video_pre_processing(request):
    print("video_pre_processing requested...")

    request_data = json.loads(request.body)
    youtube_video_url = request_data.get('youtube_video_url')

    print("downloading youtube video...")
    youtube = pytube.YouTube(youtube_video_url)
    youtube_title = youtube.title
    video = youtube.streams.get_lowest_resolution()
    video_file_path = MAIN_REPOSITORY_PATH + LOCAL_YOUTUBE_DOWNLOADS_PATH
    video_file_path = video.download(video_file_path)
    print("downloaded video_file_path: ", video_file_path)

    audio_file_path = video_file_path.split(".")[:-1][0] + ".mp3"
    cmd = get_ffmpeg_cmd(video_file_path, audio_file_path)
    system(cmd)
    print("converted audio_file_path: ", audio_file_path)

    print("uploading on google storage...")
    environ["GOOGLE_APPLICATION_CREDENTIALS"] = GCP_CREDENTIALS_FILE_NAME_FULL_PATH
    client = storage.Client(project=GCP_PROJECT_NAME).from_service_account_json(GCP_CREDENTIALS_FILE_NAME_FULL_PATH)
    bucket = client.get_bucket(GCP_BUCKET_NAME)
    destination_file_path = audio_file_path.split("/")[-1].split(".")[0]
    print("destination_file_path: ", destination_file_path)
    blob = bucket.blob(destination_file_path)
    blob.upload_from_filename(audio_file_path)
    gcp_public_url = blob.public_url
    print("uploaded file public_url: ", gcp_public_url)
    gcp_uri = get_gcp_uri(GCP_BUCKET_NAME, youtube_title)

    print("deleting video and audio...")
    remove(audio_file_path)
    remove(video_file_path)
    print("deleted.")

    print("sharing with Backend...")
    try:
        print("youtube_video_url: ", youtube_video_url)
        print("gcp_uri: ", gcp_uri)
        response = hit_add_video(youtube_video_url, gcp_uri)
        print("status_code: ", response.status_code)
        print("response.text: ", response.text)
    except Exception as err:
        print("Error occurred while adding to backend", err)
        with open(get_pending_video_path(MAIN_REPOSITORY_PATH), 'w') as f:
            json.dump(
                {
                    "youtubeVideoLink": youtube_video_url,
                    "gcpAudioLink": gcp_uri
                },
                f
            )

    return HttpResponse(request, status=201)
