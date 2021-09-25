import json
from os import system, environ, path

import pytube
import requests
from django.http import HttpResponse
from django.shortcuts import render
from django.views.decorators.csrf import csrf_exempt
from google.cloud import storage

from .core.constants import BACKEND_VIDEO_ADD_URL, BACKEND_BASE_URL


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
    video = youtube.streams.get_lowest_resolution()
    video_file_path = path.abspath(path.join(__file__, "../../../")) + "/YoutubeDownloadedVideos"
    video_file_path = video.download(video_file_path)
    print("downloaded video_file_path: ", video_file_path)

    audio_file_path = video_file_path.split(".")[:-1][0] + ".mp3"
    cmd = """ffmpeg -i \"{video_path}\" \"{audio_path}\" -y""".format(
        video_path=video_file_path, audio_path=audio_file_path
    )
    system(cmd)
    print("converted audio_file_path: ", audio_file_path)

    print("uploading on google storage...")
    environ[
        "GOOGLE_APPLICATION_CREDENTIALS"] = "/Users/saurabhchopra/dev/compsoc_freshers_hack/freshers-hack-compsoc-rhul-6b05cdc4fa4b.json"
    client = storage.Client(project='freshers-hack-compsoc-rhul').from_service_account_json(
        "/Users/saurabhchopra/dev/compsoc_freshers_hack/freshers-hack-compsoc-rhul-6b05cdc4fa4b.json")
    bucket = client.get_bucket('cs-fh-videos')
    blob = bucket.blob(audio_file_path)
    blob.upload_from_filename(audio_file_path)
    gcp_public_url = blob.public_url
    print("uploaded file public_url: ", gcp_public_url)

    print("sharing with Backend...")
    backend_url = BACKEND_VIDEO_ADD_URL + BACKEND_BASE_URL
    headers = {
        "Content-Type": "application/json"
    }
    data = {
        "youtubeVideoLink": youtube_video_url,
        "gcpAudioLink": gcp_public_url
    }
    response = requests.post(
        backend_url,
        headers=headers,
        data=json.dumps(data)
    )
    print("status_code: ", response.status_code)
    print("response.text: ", response.text)

    return HttpResponse(request, status=201)
