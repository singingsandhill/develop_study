from django.db import models

# Create your models here.

# 게시물
# - 제목, 본문, 작성자 이름, 좋아요 개수, 게시물 생성 시간

class Post(models.Model):
    title = models.CharField(max_length=128)
    body = models.CharField(max_length=1024)
    author_name = models.CharField(max_length=32)
    points = models.PositiveIntegerField(default=0)
    created_at = models.DateTimeField(auto_now=True)

    def __str__(self):
        return f"{self.pk}, {self.title}"