resource "aws_secretsmanager_secret" "tenant" {
  for_each = { for t in var.tenants : t.name => t }

  name = "/secrets/tenflo/tenant/${each.key}"
}

resource "aws_secretsmanager_secret_version" "tenant_value" {
  for_each = { for t in var.tenants : t.name => t }

  secret_id = aws_secretsmanager_secret.tenant[each.key].id
  secret_string = jsonencode({
    webhookSecret = each.value.webhookSecret
    usageApiKey   = each.value.usageApiKey
    alertBudget   = each.value.alertBudget
  })
}
