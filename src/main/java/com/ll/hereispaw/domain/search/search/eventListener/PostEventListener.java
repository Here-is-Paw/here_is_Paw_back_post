package com.ll.hereispaw.domain.search.search.eventListener;

import com.ll.hereispaw.domain.search.search.document.IndexName;
import com.ll.hereispaw.domain.search.search.document.PostDocument;
import com.ll.hereispaw.domain.search.search.service.PostDocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class PostEventListener {

    private final PostDocumentService postDocumentService;

    @KafkaListener(
            topics = "create-post",
            groupId = "search",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void createDocuments(PostDocument postDocument){
        log.debug("PostDocument: {}", postDocument);
        postDocumentService.add(postDocument, IndexName.POST.getIndexName());
    }
}
