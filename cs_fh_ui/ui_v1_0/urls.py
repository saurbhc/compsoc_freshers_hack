from django.urls import path

from .views import homepage, videos_add, videos_search, videos_list, video_pre_processing

urlpatterns = [
    # path('admin/', admin.site.urls),
    path("", homepage, name="homepage"),
    path("add", videos_add, name="videos_add"),
    path("search", videos_search, name="videos_search"),
    path("list", videos_list, name="videos_list"),
    path("video_pre_processing", video_pre_processing, name="video_pre_processing"),
]
