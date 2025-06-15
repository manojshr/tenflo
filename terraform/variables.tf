variable "tenants" {
  description = "List of tenants and their config"
  type = list(object({
    name         = string
    webhookSecret = string
    usageApiKey   = string
    alertBudget   = number
  }))

  default = [
    {
      name          = "payments"
      webhookSecret = "abcd123"
      usageApiKey   = "secret-token-xyz"
      alertBudget   = 2000
    },
    {
      name          = "accounts"
      webhookSecret = "efgh456"
      usageApiKey   = "secret-token-abc"
      alertBudget   = 1500
    }
  ]
}
