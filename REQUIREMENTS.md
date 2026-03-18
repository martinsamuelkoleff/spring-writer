# SpringWriter — A Markdown Blogging Platform

SpringWriter es una aplicación web personal que funciona como blog. El autor puede publicar artículos técnicos o reflexiones personales, y moderar comentarios de lectores desde un panel de administración integrado.
La aplicación es un monolito Spring Boot con renderizado server-side (SSR) mediante Thymeleaf. 

Las siguientes funcionalidades forman parte del alcance de la primera versión:

- Publicación de posts en formato Markdown con categorías y tags
- Listado, paginado y filtrado de posts
- Página de detalle de post con contenido renderizado
- Comentarios por post con flujo de moderación
- Panel de administración de posts protegido por autenticación

## Requerimientos

#### Tecnologías a utilizar

| Componente | Tecnología |
|---|---|
| Lenguaje | Java 21 |
| Framework | Spring Boot 3.x |
| Motor de vistas | Thymeleaf |
| Persistencia | Spring Data JPA + Hibernate |
| Base de datos | MySQL |
| Seguridad | Spring Security |
| Build tool | Maven |
| Renderizado Markdown | CommonMark |

#### Entidades 

Post

| Campo | Tipo | Restricciones | Descripción |
|---|---|---|---|
| id | UUID | PK | Identificador único |
| title | String | NOT NULL | Título del post |
| slug | String | NOT NULL, UNIQUE | URL amigable |
| content_md | Text | NOT NULL | Contenido en formato Markdown |
| excerpt | Text | | Resumen para cards y meta description |
| status | Enum | NOT NULL | `DRAFT` o `PUBLISHED` |
| published_at | Timestamp | nullable | Fecha de publicación |
| created_at | Timestamp | NOT NULL | Fecha de creación |
| updated_at | Timestamp | NOT NULL | Fecha de última modificación |
| category_id | UUID | FK → Category | Categoría del post |
 
Category
 
| Campo | Tipo | Restricciones | Descripción |
|---|---|---|---|
| id | UUID | PK | Identificador único |
| name | String | NOT NULL | Nombre visible (ej: "Técnico") |
| slug | String | NOT NULL, UNIQUE | URL amigable |
  
Tag
 
| Campo | Tipo | Restricciones | Descripción |
|---|---|---|---|
| id | UUID | PK | Identificador único |
| name | String | NOT NULL | Nombre del tag |
| slug | String | NOT NULL, UNIQUE | URL amigable |
 
Post - Tag
 
Relación ManyToMany entre `Post` y `Tag`.
 
| Campo | Tipo |
|---|---|
| post_id | UUID FK → Post |
| tag_id | UUID FK → Tag |
 
Comment
 
| Campo | Tipo | Restricciones | Descripción |
|---|---|---|---|
| id | UUID | PK | Identificador único |
| author_name | String | NOT NULL | Nombre del autor del comentario |
| author_email | String | NOT NULL | Email |
| body | Text | NOT NULL | Contenido del comentario |
| status | Enum | NOT NULL | `PENDING` o `APPROVED` |
| created_at | Timestamp | NOT NULL | Fecha de creación |
| post_id | UUID | FK → Post | Post al que pertenece |

#### Arquitectura MVC

Controllers

| Controller | Rutas | Responsabilidad |
|---|---|---|
| `HomeController` | `GET /` | Página de inicio con posts recientes |
| `BlogController` | `GET /blog` | Listado paginado de posts publicados |
| `BlogController` | `GET /blog/{slug}` | Post individual por slug |
| `BlogController` | `GET /blog/category/{cat}` | Posts filtrados por categoría |
| `BlogController` | `GET /blog/tag/{tag}` | Posts filtrados por tag |
| `CommentController` | `POST /blog/{slug}/comments` | Envío de comentario (patrón PRG) |
| `AdminController` | `GET /admin` | Dashboard del panel admin (protegido) |
| `AdminController` | `GET/POST /admin/posts/**` | Crear, editar y eliminar posts |
| `AdminController` | `GET/POST /admin/posts/{id}/comments/**` | Moderar comentarios |

Services

| Service | Métodos principales |
|---|---|
| `PostService` | `listPublished()`, `findBySlug()`, `listByCategory()`, `listByTag()`, `save()`, `delete()` |
| `CommentService` | `submit()`, `approve()`, `reject()`, `listPendingByPost()` |
| `MarkdownService` | `render(String markdown) → String html` |

Repositories

| Repository | Métodos custom relevantes |
|---|---|
| `PostRepository` | `findBySlugAndStatus()`, `findAllByStatus()`, `findByCategorySlug()`, `findByTagsSlug()` |
| `CommentRepository` | `findByPostIdAndStatus()` |
| `CategoryRepository` | `findBySlug()` |
| `TagRepository` | `findBySlug()` |

Vista

| Template | Descripción |
|---|---|
| `layout/base.html` | Layout base: nav, footer, meta tags |
| `home.html` | Página de inicio |
| `blog/list.html` | Listado de posts con cards y paginación |
| `blog/post.html` | Post individual con comentarios |
| `admin/dashboard.html` | Panel principal del admin |
| `admin/posts.html` | Listado de posts |
| `admin/post-form.html` | Formulario crear/editar post con editor Markdown |
| `admin/comments.html` | Listado de comentarios pendientes |

#### HTML

Páginas públicas

Acceso no restringido por Spring Security

| Ruta | Descripción |
|---|---|
| `/` | Hero section, posts recientes destacados, CTA a blog |
| `/blog` | Listado paginado de posts con filtros por categoría y tags |
| `/blog/{slug}` | Post completo con Markdown renderizado, tags y comentarios aprobados |
| `/blog/category/{cat}` | Posts filtrados por categoría |
| `/blog/tag/{tag}` | Posts filtrados por tag |

---

Panel de administración

Acceso restringido mediante Spring Security. Solo el autor puede acceder.

| Ruta | Descripción |
|---|---|
| `/admin` | Dashboard con resumen de posts y comentarios pendientes |
| `/admin/posts` | Listado de todos los posts (borradores y publicados) |
| `/admin/posts/new` | Formulario de creación de post con editor Markdown |
| `/admin/posts/{id}` | Edición de post existente o eliminación |
| `/admin/posts/{id}/comments/**` | Listado de comentarios con acciones aprobar / rechazar |

---

### Requisitos Funcionales

#### RF-01 · Gestión de Posts

**RF-01.1 Crear post**
El sistema debe permitir al autor crear un post con los siguientes campos: `title` (requerido), `content_md` en formato Markdown (requerido), `excerpt` (opcional), `category` (requerido), `tags` (cero o más), `status` (`DRAFT` o `PUBLISHED`). Al guardar, el sistema debe generar automáticamente un `slug` único a partir del `title`. Los timestamps `created_at` y `updated_at` deben asignarse automáticamente.

**RF-01.2 Editar post**
El sistema debe permitir al autor editar cualquier post existente. Todos los campos definidos en RF-01.1 deben ser editables. El timestamp `updated_at` debe actualizarse en cada guardado. El `slug` no debe regenerarse automáticamente al editar, para preservar las URLs existentes.

**RF-01.3 Eliminar post**
El sistema debe permitir al autor eliminar un post. Al eliminar un post, todos sus comentarios asociados deben eliminarse también.

**RF-01.4 Publicar / despublicar post**
El sistema debe permitir al autor cambiar el `status` de un post entre `DRAFT` y `PUBLISHED`. Cuando un post se publica por primera vez, `published_at` debe asignarse al timestamp actual. Despublicar un post no debe limpiar `published_at`.

**RF-01.5 Listar posts (admin)**
El panel de administración debe mostrar todos los posts sin importar su status, ordenados por `created_at` descendente, mostrando `title`, `status`, `category` y `published_at`.


#### RF-02 · Gestión de Categorías y Tags

**RF-02.1 Crear categoría**
El sistema debe permitir al autor crear una categoría con un `name`. El `slug` debe generarse automáticamente a partir del `name`.

**RF-02.2 Asignar categoría a un post**
Cada post debe tener exactamente una categoría asignada. La categoría debe seleccionarse entre las categorías existentes al crear o editar un post.

**RF-02.3 Crear tag**
El sistema debe permitir al autor crear tags escribiéndolos en el formulario del post. Si ya existe un tag con el mismo `slug`, debe reutilizarse. El `slug` debe generarse automáticamente a partir del `name`.

**RF-02.4 Asignar tags a un post**
Un post puede tener cero o más tags. Los tags deben poder asignarse y removerse al crear o editar un post.


#### RF-03 · Flujo de Comentarios

**RF-03.1 Enviar comentario**
Cualquier visitante debe poder enviar un comentario en un post publicado proporcionando: `author_name` (requerido), `author_email` (requerido, no visible públicamente), `body` (requerido). Al enviarse, el comentario debe guardarse con `status = PENDING` y no debe ser visible públicamente hasta ser aprobado.

**RF-03.2 Confirmación tras el envío**
Luego de enviar un comentario, el sistema debe redirigir al visitante de vuelta a la página del post (patrón PRG) y mostrar un mensaje confirmando que el comentario está pendiente de moderación.

**RF-03.3 Aprobar comentario**
El autor debe poder aprobar un comentario `PENDING` desde el panel de administración. Una vez aprobado, el comentario debe ser inmediatamente visible en la página del post.

**RF-03.4 Rechazar comentario**
El autor debe poder rechazar y eliminar un comentario `PENDING` desde el panel de administración.

**RF-03.5 Mostrar comentarios aprobados**
La página del post debe mostrar todos los comentarios con `status = APPROVED` de ese post, ordenados por `created_at` ascendente.


#### RF-04 · Navegación Pública del Blog

**RF-04.1 Página de listado de posts**
La página `/blog` debe mostrar únicamente posts publicados, ordenados por `published_at` descendente, paginados. Cada post debe mostrarse como una card con: `title`, `published_at`, `category`, `tags` y `excerpt`.

**RF-04.2 Página de detalle de post**
La página `/blog/{slug}` debe mostrar el contenido completo de un post publicado con el campo `content_md` renderizado como HTML. Si el slug no corresponde a ningún post publicado, el sistema debe retornar una respuesta `404`.

**RF-04.3 Filtrar por categoría**
La página `/blog/category/{cat}` debe mostrar únicamente posts publicados pertenecientes a la categoría indicada, con el mismo layout de cards y paginación que RF-04.1.

**RF-04.4 Filtrar por tag**
La página `/blog/tag/{tag}` debe mostrar únicamente posts publicados asociados al tag indicado, con el mismo layout de cards y paginación que RF-04.1.

**RF-04.5 Visibilidad de borradores**
Los posts con `status = DRAFT` nunca deben ser accesibles desde ninguna ruta pública, independientemente de si el slug es conocido.


### Flujos de la aplicación

#### FL-01 · Navegación pública

**FL-01.1 Ver listado de posts**
1. El visitante abre `/blog`.
2. `BlogController` llama a `PostService.listPublished()` con paginación.
3. El sistema muestra las cards de posts publicados ordenados por `published_at` descendente.
4. El visitante puede navegar entre páginas con los controles de paginación.

**FL-01.2 Filtrar posts por categoría**
1. El visitante hace clic en una categoría desde una card o la navegación.
2. El navegador abre `/blog/category/{cat}`.
3. `BlogController` llama a `PostService.listByCategory(cat)`.
4. El sistema muestra únicamente los posts publicados de esa categoría.

**FL-01.3 Filtrar posts por tag**
1. El visitante hace clic en un tag desde una card o la página de un post.
2. El navegador abre `/blog/tag/{tag}`.
3. `BlogController` llama a `PostService.listByTag(tag)`.
4. El sistema muestra únicamente los posts publicados con ese tag.

**FL-01.4 Leer un post**
1. El visitante hace clic en la card de un post.
2. El navegador abre `/blog/{slug}`.
3. `BlogController` llama a `PostService.findBySlug(slug)`.
4. Si el post no existe o está en `DRAFT`, el sistema retorna `404`.
5. `MarkdownService.render(content_md)` convierte el contenido a HTML.
6. `CommentService.listApprovedByPost(postId)` obtiene los comentarios aprobados.
7. El sistema muestra el post completo con sus comentarios.


#### FL-02 · Enviar un comentario

1. El visitante lee un post y completa el formulario con `author_name`, `author_email` y `body`.
2. El navegador envía `POST /blog/{slug}/comments`.
3. `CommentController` valida los campos del formulario.
4. Si la validación falla, el sistema retorna la página del post con los errores marcados en el formulario.
5. Si la validación es exitosa, `CommentService.submit()` guarda el comentario con `status = PENDING`.
6. El sistema redirige al visitante a `GET /blog/{slug}` (patrón PRG).
7. La página del post muestra un mensaje de confirmación indicando que el comentario está pendiente de moderación.
8. El comentario no es visible públicamente hasta ser aprobado por el autor.


#### FL-03 · Login al panel de administración

1. El autor abre `/admin`.
2. Spring Security intercepta la request y redirige a `/login`.
3. El autor ingresa sus credenciales (usuario y contraseña).
4. Si las credenciales son incorrectas, el sistema muestra un mensaje de error y vuelve al formulario de login.
5. Si las credenciales son correctas, Spring Security redirige al autor a `/admin`.
6. El autor permanece autenticado hasta cerrar sesión o que la sesión expire.


#### FL-04 · Crear y publicar un post

1. El autor navega a `/admin/posts/new`.
2. El sistema muestra el formulario vacío con campos: `title`, `content_md`, `excerpt`, `category`, `tags` y `status`.
3. El autor completa los campos y selecciona `status = DRAFT` o `PUBLISHED`.
4. El autor envía el formulario.
5. `AdminController` valida los campos.
6. Si la validación falla, el sistema retorna el formulario con los errores marcados.
7. Si la validación es exitosa, `PostService.save()` genera el `slug` desde el `title` y persiste el post.
8. Si el `status` es `PUBLISHED`, se asigna `published_at` al timestamp actual.
9. El sistema redirige al autor a `/admin/posts`.


#### FL-05 · Editar y eliminar un post

**FL-05.1 Editar post**
1. El autor navega a `/admin/posts` y selecciona un post.
2. El sistema abre `/admin/posts/{id}` con el formulario precargado con los datos actuales.
3. El autor modifica los campos deseados y envía el formulario.
4. `AdminController` valida los campos.
5. Si la validación falla, el sistema retorna el formulario con los errores marcados.
6. Si la validación es exitosa, `PostService.save()` persiste los cambios y actualiza `updated_at`.
7. El `slug` no se regenera para preservar las URLs existentes.
8. El sistema redirige al autor a `/admin/posts`.

**FL-05.2 Eliminar post**
1. El autor navega a `/admin/posts` y selecciona la opción eliminar en un post.
2. El sistema solicita confirmación al autor.
3. Si el autor confirma, `PostService.delete()` elimina el post y todos sus comentarios asociados.
4. El sistema redirige al autor a `/admin/posts`.


#### FL-06 · Moderar comentarios

1. El autor navega a `/admin/posts/{id}/comments`.
2. El sistema muestra los comentarios `PENDING` de ese post con `author_name`, `author_email`, `body` y `created_at`.
3. El autor puede tomar una de dos acciones por cada comentario:
   - **Aprobar**: `CommentService.approve()` cambia el `status` a `APPROVED`. El comentario pasa a ser visible públicamente en el post.
   - **Rechazar**: `CommentService.reject()` elimina el comentario permanentemente.
4. El sistema recarga la lista de comentarios pendientes.
