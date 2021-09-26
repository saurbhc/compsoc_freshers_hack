def get_gcp_uri(gcp_bucket_name, youtube_title):
    return "gs://" + gcp_bucket_name + "/" + youtube_title
