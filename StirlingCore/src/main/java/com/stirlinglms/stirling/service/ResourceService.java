package com.stirlinglms.stirling.service;

import com.google.cloud.storage.Storage;
import com.stirlinglms.stirling.entity.res.Resource;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.repository.ResourceRepository;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.Set;

public interface ResourceService extends RepositoryService<Resource, ResourceRepository> {

    Resource uploadResource(User owner, File file) throws FileNotFoundException;

    Resource uploadResource(User owner, MultipartFile file) throws FileNotFoundException;

    Resource uploadResource(User owner, Path file) throws FileNotFoundException;

    byte[] getRawResource(Resource resource);

    Resource getByBlobId(long id);

    Set<Resource> getAll();

    Storage getStorage();
}
