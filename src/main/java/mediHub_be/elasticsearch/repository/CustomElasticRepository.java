package mediHub_be.elasticsearch.repository;

import mediHub_be.elasticsearch.document.*;

import java.util.List;

public interface CustomElasticRepository {

    List<AnonymousBoardDocument> findAnonymousBoardDocumentByKeywordInField(String fieldName, String keyword);
    List<CaseSharingDocument> findCaseSharingDocumentByKeywordInField(String fieldName, String keyword);
    List<CpDocument> findCpDocumentByKeywordInField(String fieldName, String keyword);
    List<JournalDocument> findJournalDocumentByKeywordInField(String fieldName, String keyword);
    List<MedicalLifeDocument> findMedicalLifeDocumentByKeywordInField(String fieldName, String keyword);
}