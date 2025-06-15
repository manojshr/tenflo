resource "aws_iam_role" "tenant_role" {
  for_each = { for t in var.tenants : t.name => t }

  name = "tenflo-tenant-${each.key}-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Effect = "Allow",
        Principal = {
          Service = "ec2.amazonaws.com"
        },
        Action = "sts:AssumeRole"
      }
    ]
  })
}

resource "aws_iam_policy" "tenant_secret_policy" {
  for_each = { for t in var.tenants : t.name => t }

  name = "tenflo-${each.key}-secret-policy"

  policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Effect   = "Allow",
        Action   = ["secretsmanager:GetSecretValue"],
        Resource = aws_secretsmanager_secret.tenant[each.key].arn
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "attach_policy" {
  for_each = { for t in var.tenants : t.name => t }

  role       = aws_iam_role.tenant_role[each.key].name
  policy_arn = aws_iam_policy.tenant_secret_policy[each.key].arn
}
