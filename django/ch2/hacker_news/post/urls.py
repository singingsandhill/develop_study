from django.urls import path
from post import views

urlpatterns =[
    #path('index/', views.index_view, name='psst_index')
    path('', views.PostListView.as_view(), name='posts'),
    path("create/", views.PostCreateView.as_view(), name="post_create"),# 전체 게시글 목록
    path('<int:post_id>/',views.PostDetailView.as_view(), name="post"),   # 상세 게시글 목록
    path("<int:post_id>/like/", views.PostLikeView.as_view(), name="post_like"),
]