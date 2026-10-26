# Image used by the CI (and locally) to run the Ansible playbook
FROM python:3.12-slim

# ssh client: Ansible connects to the server over SSH
RUN apt-get update \
 && apt-get install -y --no-install-recommends openssh-client \
 && rm -rf /var/lib/apt/lists/*

# full "ansible" package = ansible-core + collections (community.docker, ...)
RUN pip install --no-cache-dir ansible

ENV ANSIBLE_HOST_KEY_CHECKING=False
WORKDIR /ansible
ENTRYPOINT ["ansible-playbook"]
