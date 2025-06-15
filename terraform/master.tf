
resource "aws_iam_user" "master_user" {
  name = "multi-tenant-deployment-master-user"
}

resource "aws_iam_access_key" "master_user_key" {
  user = aws_iam_user.master_user.name
}

resource "aws_iam_role" "master_role" {
  name = "multi-tenant-deployment-master-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [{
      Effect = "Allow",
      Principal = {
        AWS = "arn:aws:iam::000000000000:user/multi-tenant-deployment-master-user"
      },
      Action = "sts:AssumeRole"
    }]
  })
}

resource "aws_secretsmanager_secret" "master_user_secret" {
  name = "/secrets/tenflo/master/multi-tenant-deployment"
}

resource "aws_secretsmanager_secret_version" "master_user_secret_version" {
  secret_id     = aws_secretsmanager_secret.master_user_secret.id
  secret_string = jsonencode({
    accessKeyId     = aws_iam_access_key.master_user_key.id,
    secretAccessKey = aws_iam_access_key.master_user_key.secret
  })
}
