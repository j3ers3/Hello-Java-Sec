docker build -t javasec . && docker run -d -p 80:8888 -v logs:/logs javasec
