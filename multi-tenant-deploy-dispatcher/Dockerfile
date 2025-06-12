FROM hashicorp/terraform:1.11.4

# Copy content
COPY terraform/ /terraform/
WORKDIR /terraform

ENTRYPOINT ["/terraform/entrypoint.sh"]
