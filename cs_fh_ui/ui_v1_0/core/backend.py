import json
import requests

from .constants import BACKEND_VIDEO_ADD_URL, BACKEND_BASE_URL


def hit_add_video(youtube_video_url, gcp_uri):
    backend_url = BACKEND_BASE_URL + BACKEND_VIDEO_ADD_URL
    headers = {
        "Content-Type": "application/json"
    }
    data = {
        "youtubeVideoLink": youtube_video_url,
        "gcpAudioLink": gcp_uri
    }
    response = requests.post(
        backend_url,
        headers=headers,
        data=json.dumps(data)
    )

    return response
