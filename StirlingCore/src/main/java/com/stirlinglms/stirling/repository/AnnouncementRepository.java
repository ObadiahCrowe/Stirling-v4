package com.stirlinglms.stirling.repository;

import com.stirlinglms.stirling.entity.announcement.Announcement;
import com.stirlinglms.stirling.entity.user.User;
import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository;

import java.util.List;

public interface AnnouncementRepository extends DatastoreRepository<Announcement, Long> {

    List<Announcement> findAllByTitle(String title);

    List<Announcement> findAllByPoster(User user);
}
