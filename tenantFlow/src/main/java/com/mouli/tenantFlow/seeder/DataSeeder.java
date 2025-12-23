package com.mouli.tenantFlow.seeder;

import com.mouli.tenantFlow.entity.Project;
import com.mouli.tenantFlow.entity.Task;
import com.mouli.tenantFlow.entity.Tenant;
import com.mouli.tenantFlow.entity.User;
import com.mouli.tenantFlow.repository.ProjectRepository;
import com.mouli.tenantFlow.repository.TaskRepository;
import com.mouli.tenantFlow.repository.TenantRepository;
import com.mouli.tenantFlow.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Idempotent data seeder for local/dev/testing.
 *
 * Creates:
 *  - Super admin (tenant_id = NULL)
 *  - Demo tenant (subdomain = "demo")
 *  - tenant admin (admin@demo.com)
 *  - 2 regular users (user1@demo.com, user2@demo.com)
 *  - 2 projects for demo tenant
 *  - 5 tasks distributed across projects
 *
 * Runs only if demo tenant does not already exist.
 */
@Component
@Order(1)
public class DataSeeder implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository,
                      TenantRepository tenantRepository,
                      ProjectRepository projectRepository,
                      TaskRepository taskRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tenantRepository = tenantRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        final String demoSubdomain = "demo";

        // Idempotency check: if demo tenant already exists, do nothing
        boolean demoExists = tenantRepository.existsBySubdomain(demoSubdomain);
        if (demoExists) {
            log.info("Demo tenant already exists — skipping seed.");
            return;
        }

        log.info("Seeding initial data...");

        // 1) Super Admin (tenant_id = null)
        User superAdmin = new User();
        superAdmin.setId(UUID.randomUUID());
        superAdmin.setTenant(null); // super admin has no tenant
        superAdmin.setEmail("superadmin@system.com");
        superAdmin.setPasswordHash(passwordEncoder.encode("Admin@123"));
        superAdmin.setFullName("System Admin");
        superAdmin.setRole("super_admin");
        superAdmin.setActive(true);
        userRepository.save(superAdmin);

        // 2) Demo Tenant
        Tenant demo = new Tenant();
        demo.setId(UUID.randomUUID());
        demo.setName("Demo Company");
        demo.setSubdomain(demoSubdomain);
        demo.setStatus("active");
        demo.setSubscriptionPlan("pro");
        demo.setMaxUsers(25);
        demo.setMaxProjects(15);
        tenantRepository.save(demo);

        // 3) Tenant Admin
        User admin = new User();
        admin.setId(UUID.randomUUID());
        admin.setTenant(demo);
        admin.setEmail("admin@demo.com");
        admin.setPasswordHash(passwordEncoder.encode("Demo@123"));
        admin.setFullName("Demo Admin");
        admin.setRole("tenant_admin");
        admin.setActive(true);
        userRepository.save(admin);

        // 4) Two regular users
        User user1 = new User();
        user1.setId(UUID.randomUUID());
        user1.setTenant(demo);
        user1.setEmail("user1@demo.com");
        user1.setPasswordHash(passwordEncoder.encode("User@123"));
        user1.setFullName("Demo User One");
        user1.setRole("user");
        user1.setActive(true);

        User user2 = new User();
        user2.setId(UUID.randomUUID());
        user2.setTenant(demo);
        user2.setEmail("user2@demo.com");
        user2.setPasswordHash(passwordEncoder.encode("User@123"));
        user2.setFullName("Demo User Two");
        user2.setRole("user");
        user2.setActive(true);

        userRepository.saveAll(List.of(user1, user2));

        // 5) Two sample projects (createdBy = admin)
        Project p1 = new Project();
        p1.setId(UUID.randomUUID());
        p1.setTenant(demo);
        p1.setName("Project Alpha");
        p1.setDescription("First demo project");
        p1.setStatus("active");
        p1.setCreatedBy(admin);

        Project p2 = new Project();
        p2.setId(UUID.randomUUID());
        p2.setTenant(demo);
        p2.setName("Project Beta");
        p2.setDescription("Second demo project");
        p2.setStatus("active");
        p2.setCreatedBy(admin);

        projectRepository.saveAll(List.of(p1, p2));

        // 6) Five tasks distributed across projects
        List<Task> tasks = new ArrayList<>();

        Task t1 = new Task();
        t1.setId(UUID.randomUUID());
        t1.setProject(p1);
        t1.setTenant(demo);
        t1.setTitle("Design homepage mockup");
        t1.setDescription("Create high-fidelity homepage design");
        t1.setStatus("todo");
        t1.setPriority("high");
        t1.setAssignedTo(user1);
        t1.setDueDate(LocalDate.now().plusDays(7));
        tasks.add(t1);

        Task t2 = new Task();
        t2.setId(UUID.randomUUID());
        t2.setProject(p1);
        t2.setTenant(demo);
        t2.setTitle("Implement login API");
        t2.setDescription("Backend authentication endpoints");
        t2.setStatus("in_progress");
        t2.setPriority("medium");
        t2.setAssignedTo(user2);
        t2.setDueDate(LocalDate.now().plusDays(10));
        tasks.add(t2);

        Task t3 = new Task();
        t3.setId(UUID.randomUUID());
        t3.setProject(p2);
        t3.setTenant(demo);
        t3.setTitle("Set up database migrations");
        t3.setDescription("Add Flyway migrations and seed runner");
        t3.setStatus("todo");
        t3.setPriority("medium");
        t3.setAssignedTo(admin);
        t3.setDueDate(LocalDate.now().plusDays(5));
        tasks.add(t3);

        Task t4 = new Task();
        t4.setId(UUID.randomUUID());
        t4.setProject(p2);
        t4.setTenant(demo);
        t4.setTitle("Create project README");
        t4.setDescription("Document features and how to run locally");
        t4.setStatus("todo");
        t4.setPriority("low");
        t4.setAssignedTo(user1);
        t4.setDueDate(LocalDate.now().plusDays(14));
        tasks.add(t4);

        Task t5 = new Task();
        t5.setId(UUID.randomUUID());
        t5.setProject(p2);
        t5.setTenant(demo);
        t5.setTitle("Write unit tests for services");
        t5.setDescription("Add basic unit tests for user and tenant services");
        t5.setStatus("todo");
        t5.setPriority("high");
        t5.setAssignedTo(null); // unassigned
        t5.setDueDate(LocalDate.now().plusDays(12));
        tasks.add(t5);

        taskRepository.saveAll(tasks);

        log.info("✅ Database seeded successfully: superAdmin={}, tenant={}, admin={}, users=[{}, {}], projects=[{}, {}], tasks={}",
                superAdmin.getEmail(), demo.getSubdomain(), admin.getEmail(),
                user1.getEmail(), user2.getEmail(),
                p1.getName(), p2.getName(), tasks.size());
    }
}
