package project.fsurvey.business.abstracts;

import project.fsurvey.entities.concretes.users.Admin;

import java.util.UUID;

public interface AdminService extends CrudService<Admin> {
    Admin findByUUID(UUID uuid);
}
