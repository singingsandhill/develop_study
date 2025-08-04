from django.urls import path
from post import views

urlpatterns =[
    #path('index/', views.index_view, name='psst_index')
    path('', views.posts_views, name='posts'),              # 전체 게시글 목록
    path('<int:post_id>/',views.post_view, name="post")   # 상세 게시글 목록
]