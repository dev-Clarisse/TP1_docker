FROM httpd:alpine

# Copier la page d'accueil par défaut d'Apache
COPY index.html /usr/local/apache2/htdocs/index.html

# Copier le fichier de configuration modifié (avec le reverse proxy)
COPY httpd.conf /usr/local/apache2/conf/httpd.conf