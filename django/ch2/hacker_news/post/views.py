from django.http import HttpResponse, HttpResponseBadRequest
from django.shortcuts import render, get_object_or_404
from flask import redirect

from post.forms import PostForm
from post.models import Post


# Create your views here.
# def index_view(request):
#    return HttpResponse("Hello, Hackers!")

def posts_views(request):
    if request.method == 'GET':
        posts = Post.objects.all()
        context = {'posts': posts, "form": PostForm}
        result = ", ".join([p.title for p in posts])
        return render(request, 'post_list.html', context)
    elif request.method == 'POST':
        form = PostForm(request.POST)
        if form.is_valid():
            title = form.cleaned_data['title']
            body = form.cleaned_data['body']
            author_name = form.cleaned_data['author_name']

            # title = request.POST.get('title')
            # body = request.POST.get('body')
            # author_name = request.POST.get('author_name')

            # if not (type(title) is str and 0 < len(title) <= 128):
            #     return HttpResponseBadRequest('Invalid title')
            # if not (type(body) is str and 0 < len(body) <= 1024):
            #     return HttpResponseBadRequest('Invalid body')
            # if not (type(author_name) is str and 0 < len(author_name) <= 32):
            #     return HttpResponseBadRequest('Invalid author_name')

            post = Post.objects.create(title=title, body=body, author_name=author_name)
            context = {'post': post}

            return render(request, 'post_detail.html', context)

        #posts = Post.objects.all()
        #context = {'posts': posts, "form": PostForm}
        #return render(request, 'post_list.html', context)
        return redirect('posts')


def post_view(request, post_id):
    post = get_object_or_404(Post, id=post_id)
    context = {"post": post}
    return render(request, "post_detail.html", context)
