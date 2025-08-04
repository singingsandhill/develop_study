from django import forms
from post.models import Post
#class PostForm(forms.Form):
#    title = forms.CharField(max_length=128, label="제목")
#    body = forms.CharField(max_length=1024 , label= "본문")
#    author_name = forms.CharField(max_length=32, label="작성자")

class PostForm(forms.ModelForm):
    class Meta:
        model = Post
        fields = ['title','body','author_name']
        labels = {
            'title': '제목',
            'body': '본문',
            'author_name': '작성자',
        }