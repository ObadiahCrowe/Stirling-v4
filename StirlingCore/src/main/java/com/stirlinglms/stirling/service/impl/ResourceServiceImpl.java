package com.stirlinglms.stirling.service.impl;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.stirlinglms.stirling.Stirling;
import com.stirlinglms.stirling.entity.res.Resource;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.repository.ResourceRepository;
import com.stirlinglms.stirling.service.ResourceService;
import com.stirlinglms.stirling.util.FileUtil;
import com.stirlinglms.stirling.util.UpdateLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ResourceServiceImpl implements ResourceService {

    private static final String[] VALID_FIELDS = new String[] {
      "name",
      "sharedWith"
    };

    private static final AtomicReference<Credentials> CLOUD_CREDENTIALS = new AtomicReference<>(null);
    private static final AtomicReference<Storage> STORAGE_IMPL = new AtomicReference<>(null);

    private final ResourceRepository resourceRepository;

    @Autowired
    ResourceServiceImpl(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;

        try {
            CLOUD_CREDENTIALS.compareAndSet(null, GoogleCredentials
              .fromStream(Stirling.class.getClassLoader().getResourceAsStream("cloud_secret.json"))
              .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        STORAGE_IMPL.compareAndSet(null, StorageOptions.newBuilder().setCredentials(CLOUD_CREDENTIALS.get())
          .setProjectId("adc18e7d9668dd7783e529a7841ce5cbae378f72").build().getService());
    }
    
    @Override
    public Resource uploadResource(User owner, File file) throws FileNotFoundException {
        Bucket bucket = this.getStorage().get("stirlingstorage");

        try {
            Blob blob = bucket.create(file.getName().toLowerCase(), new FileInputStream(file));

            return new Resource(owner, file.getName(), blob.getBlobId());
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(e.getMessage());
        }
    }

    @Override
    public Resource uploadResource(User owner, MultipartFile file) throws FileNotFoundException {
        return this.uploadResource(owner, FileUtil.fromMultipart(file));
    }

    @Override
    public Resource uploadResource(User owner, Path file) throws FileNotFoundException {
        return this.uploadResource(owner, file.toFile());
    }

    @Override
    public byte[] getRawResource(Resource resource) {
        Blob blob = this.getStorage().get(resource.getBlobId());

        return blob.getContent();
    }

    @Override
    public Resource getByBlobId(long id) {
        return this.getAll().stream().filter(r -> r.getBlobId().getGeneration() == id).findFirst().orElse(null);
    }

    @Override
    public Resource update(Resource entity, UpdateLevel level) throws Exception {
        return this.resourceRepository.performTransaction((repository) -> repository.save(entity));
    }

    @Override
    public Resource delete(Resource entity) {
        this.getStorage().delete(entity.getBlobId());
        this.resourceRepository.delete(entity);

        return entity;
    }

    @Override
    public Resource getById(Long id) {
        return this.resourceRepository.findById(id).orElse(null);
    }

    @Override
    public Set<Resource> getAll() {
        return Sets.newConcurrentHashSet(this.resourceRepository.findAll());
    }

    @Override
    public Storage getStorage() {
        return STORAGE_IMPL.get();
    }

    @Override
    public ResourceRepository getRepository() {
        return this.resourceRepository;
    }
}
