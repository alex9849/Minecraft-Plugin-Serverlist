package net.alex9849.web.db;

import net.alex9849.web.model.PluginInstallation;
import org.springframework.data.repository.CrudRepository;

public interface PluginInstallationRepo extends CrudRepository<PluginInstallation, String> {
}
