from datetime import datetime

from .constants import LOCAL_PENDING_BACKEND_VID_PATH


def get_ffmpeg_cmd(video_file_path, audio_file_path):
    return """ffmpeg -i \"{video_path}\" \"{audio_path}\" -y""".format(
        video_path=video_file_path, audio_path=audio_file_path
    )


def get_pending_video_path(main_repository_path):
    return main_repository_path + LOCAL_PENDING_BACKEND_VID_PATH + "/pending-add-video-" + str(datetime.now()) + ".json"
