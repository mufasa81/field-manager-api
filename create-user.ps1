$body = @{
    name = '박준범'
    email = 'mufasa81@gmail.com'
    password = '123456'
    role = 'Admin'
} | ConvertTo-Json

Invoke-RestMethod -Uri http://127.0.0.1:8080/api/users/create -Method Post -ContentType 'application/json' -Body $body
