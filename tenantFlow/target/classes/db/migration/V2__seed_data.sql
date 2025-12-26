-- ===============================
-- SUPER ADMIN (NO TENANT)
-- ===============================
INSERT INTO users (
    id, tenant_id, email, password_hash,
    full_name, role, is_active, created_at
) VALUES (
    '11111111-1111-1111-1111-111111111111',
    NULL,
    'superadmin@system.com',
    '$2a$10$6c2bqk2Q6yG2eQm/9pRXG.NZL9KXkt8cZs1vZ9YuqbWtaXDpUe1Gi',
    'Super Admin',
    'SUPER_ADMIN',
    TRUE,
    NOW()
);

-- ===============================
-- TENANT: DEMO COMPANY
-- ===============================
INSERT INTO tenants (
    id, name, subdomain, status,
    subscription_plan, max_users, max_projects,
    created_at
) VALUES (
    '22222222-2222-2222-2222-222222222222',
    'Demo Company',
    'demo',
    'ACTIVE',
    'PRO',
    25,
    15,
    NOW()
);

-- ===============================
-- TENANT ADMIN
-- ===============================
INSERT INTO users (
    id, tenant_id, email, password_hash,
    full_name, role, is_active, created_at
) VALUES (
    '33333333-3333-3333-3333-333333333333',
    '22222222-2222-2222-2222-222222222222',
    'admin@demo.com',
    '$2a$10$3cFqY2T8o5RXzU3pL9Yl0uAqVb7xkLr2JH3V8w9cG2FZ0S9sZ7B3K',
    'Demo Admin',
    'TENANT_ADMIN',
    TRUE,
    NOW()
);

-- ===============================
-- REGULAR USERS
-- ===============================
INSERT INTO users (
    id, tenant_id, email, password_hash,
    full_name, role, is_active, created_at
) VALUES
(
    '44444444-4444-4444-4444-444444444444',
    '22222222-2222-2222-2222-222222222222',
    'user1@demo.com',
    '$2a$10$G6H3L2Pz9KxY8M7tF0E9Vuxw5n8MZpYc2P5dXk6RrQJq1vZ9PZ0yS',
    'Demo User One',
    'USER',
    TRUE,
    NOW()
),
(
    '55555555-5555-5555-5555-555555555555',
    '22222222-2222-2222-2222-222222222222',
    'user2@demo.com',
    '$2a$10$G6H3L2Pz9KxY8M7tF0E9Vuxw5n8MZpYc2P5dXk6RrQJq1vZ9PZ0yS',
    'Demo User Two',
    'USER',
    TRUE,
    NOW()
);

-- ===============================
-- PROJECTS
-- ===============================
INSERT INTO projects (
    id, tenant_id, name, description,
    status, created_by, created_at
) VALUES
(
    '66666666-6666-6666-6666-666666666666',
    '22222222-2222-2222-2222-222222222222',
    'Project Alpha',
    'First demo project',
    'ACTIVE',
    '33333333-3333-3333-3333-333333333333',
    NOW()
),
(
    '77777777-7777-7777-7777-777777777777',
    '22222222-2222-2222-2222-222222222222',
    'Project Beta',
    'Second demo project',
    'ACTIVE',
    '33333333-3333-3333-3333-333333333333',
    NOW()
);

-- ===============================
-- TASKS
-- ===============================
INSERT INTO tasks (
    id, tenant_id, project_id, title,
    description, status, priority,
    assigned_to, due_date, created_at
) VALUES
(
    '88888888-8888-8888-8888-888888888888',
    '22222222-2222-2222-2222-222222222222',
    '66666666-6666-6666-6666-666666666666',
    'Setup backend',
    'Initialize Spring Boot project',
    'IN_PROGRESS',
    'HIGH',
    '44444444-4444-4444-4444-444444444444',
    CURRENT_DATE + INTERVAL '7 days',
    NOW()
),
(
    '99999999-9999-9999-9999-999999999999',
    '22222222-2222-2222-2222-222222222222',
    '77777777-7777-7777-7777-777777777777',
    'Design DB schema',
    'Create Flyway migrations',
    'COMPLETED',
    'MEDIUM',
    '55555555-5555-5555-5555-555555555555',
    CURRENT_DATE + INTERVAL '3 days',
    NOW()
);
