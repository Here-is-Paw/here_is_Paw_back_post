package com.ll.hereispaw.domain.mapPost.mapPost.service;

import com.ll.hereispaw.domain.find.find.repository.FindRepository;
import com.ll.hereispaw.domain.mapPost.mapPost.dto.request.MapPostRequest;
import com.ll.hereispaw.domain.missing.missing.entity.Missing;
import com.ll.hereispaw.domain.missing.missing.repository.MissingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MapPostService {
    private final MissingRepository missingRepository;
    private final FindRepository findRepository;

    public List<Missing> allSearch(MapPostRequest mapPostRequest) {
        return missingRepository.findWithinRadius(mapPostRequest.getLatitude(), mapPostRequest.getLongitude(), mapPostRequest.getRadius());
    }
}
