# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Development Commands

### Running the Django Application
- `python manage.py runserver` - Start the development server (default: http://127.0.0.1:8000/)
- `python manage.py runserver <port>` - Start server on specific port

### Database Management
- `python manage.py makemigrations` - Create migration files for model changes
- `python manage.py migrate` - Apply migrations to database
- `python manage.py showmigrations` - Show migration status
- `python manage.py sqlmigrate <app> <migration_number>` - Show SQL for specific migration

### Testing
- `python manage.py test` - Run all tests
- `python manage.py test <app_name>` - Run tests for specific app
- `python manage.py test <app_name.tests.TestClass>` - Run specific test class

### Admin and User Management
- `python manage.py createsuperuser` - Create Django admin superuser
- `python manage.py collectstatic` - Collect static files (when needed)

### Shell and Debugging
- `python manage.py shell` - Open Django shell with project context
- `python manage.py dbshell` - Open database shell

## Project Architecture

### Core Structure
This is a Django 5.2.4 project implementing a Hacker News-like application with the following structure:

- **config/**: Main Django project configuration
  - `settings.py`: Project settings (uses SQLite, Korean comments)
  - `urls.py`: Main URL routing (admin + posts app)
  - `wsgi.py`/`asgi.py`: WSGI/ASGI application entry points

- **post/**: Django app handling post functionality
  - `models.py`: Post model with title, body, author_name, points, created_at
  - `views.py`: Function-based views for listing and creating posts
  - `urls.py`: App-specific URL patterns
  - `templates/`: HTML templates for post list and detail views

### Key Models
- **Post**: Core model representing a post/article
  - `title` (CharField, max 128 chars)
  - `body` (CharField, max 1024 chars) 
  - `author_name` (CharField, max 32 chars)
  - `points` (PositiveIntegerField, default 0)
  - `created_at` (DateTimeField, auto_now=True)

### URL Structure
- `/admin/` - Django admin interface
- `/posts/` - Post list view (GET) and post creation (POST)
- `/posts/<int:post_id>/` - Individual post detail view

### Views Architecture
- `posts_views()`: Handles both GET (list all posts) and POST (create new post) requests
- `post_view()`: Displays individual post details
- Uses Django's render() and get_object_or_404() patterns
- Input validation for POST requests with appropriate error responses

### Database
- Uses SQLite (db.sqlite3) as default database
- Single Post model with basic fields
- Auto-generated primary keys and timestamps

### Templates
- Basic HTML templates with Korean text
- Uses Django template language for dynamic content
- Form handling with CSRF protection
- Simple navigation between list and detail views

## Development Notes
- Project uses Korean comments in code and templates
- All hosts allowed in ALLOWED_HOSTS (development setting)
- DEBUG mode enabled (development setting)
- Secret key exposed (development only - should be secured for production)
- No requirements.txt file - dependencies need to be determined