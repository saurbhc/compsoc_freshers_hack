import json
import requests

from .constants import BACKEND_BASE_URL, BACKEND_VIDEO_ADD_URL, BACKEND_VIDEO_SEARCH_URL, BACKEND_VIDEO_LIST_URL


def hit_add_video(youtube_video_url, gcp_uri):
    backend_url = BACKEND_BASE_URL + BACKEND_VIDEO_ADD_URL
    headers = {
        'Content-Type': 'application/json'
    }
    payload = json.dumps({
        "youtubeVideoLink": youtube_video_url,
        "gcpAudioLink": gcp_uri
    })

    response = requests.request("POST", backend_url, headers=headers, data=payload)

    return response


def hit_search_video(search_string):
    backend_url = BACKEND_BASE_URL + BACKEND_VIDEO_SEARCH_URL
    headers = {
        'Content-Type': 'application/json'
    }
    payload = json.dumps({
        "searchString": search_string
    })

    response = requests.request("POST", backend_url, headers=headers, data=payload)

    return response


def hit_list_video():
    backend_url = BACKEND_BASE_URL + BACKEND_VIDEO_LIST_URL
    headers = {
        'Content-Type': 'application/json'
    }

    response = requests.request("GET", backend_url, headers=headers)

    return response
