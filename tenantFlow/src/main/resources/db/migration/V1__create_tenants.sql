CREATE TABLE tenants (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    subdomain VARCHAR(100) NOT NULL UNIQUE,
    status VARCHAR(20) NOT NULL CHECK (status IN ('active', 'suspended', 'trial')),
    subscription_plan VARCHAR(20) NOT NULL CHECK (subscription_plan IN ('free', 'pro', 'enterprise')),
    max_users INT NOT NULL,
    max_projects INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);