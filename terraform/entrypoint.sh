#!/bin/sh

cd /terraform

# You can customize these commands
terraform init
terraform plan

# Optional auto-approve apply
terraform apply -auto-approve
