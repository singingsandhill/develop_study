from django.urls import path, include
from user import views

urlpatterns = [
    path('', views.HomeView.as_view(), name='home'),
    # path('users/password_change/', views.MyPasswordChangeView.as_view()),
    path('users/', include('django.contrib.auth.urls')),
    path('users/sign_up/', views.SignupView.as_view(), name='sign_up'),
    path('users/social/kakao/login/', views.KaKaoSocialLoginView.as_view(), name="kakao_social_login"),
    path("users/social/kakao/callback/", views.KakaoSocialCallbackView.as_view(), name="kakao_social_login_callback"),
]
