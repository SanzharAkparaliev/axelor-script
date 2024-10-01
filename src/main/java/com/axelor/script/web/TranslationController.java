package com.axelor.script.web;

import com.axelor.apps.base.db.repo.CityRepository;
import com.axelor.apps.base.db.repo.CountryRepository;
import com.axelor.meta.db.MetaTranslation;
import com.axelor.meta.db.repo.MetaTranslationRepository;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.axelor.script.service.DictionaryService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class TranslationController {

  private static final Logger logger = LoggerFactory.getLogger(TranslationController.class);

  private final DictionaryService dictionaryService;
  private final MetaTranslationRepository metaTranslationRepository;
  private final CountryRepository countryRepository;
  private final CityRepository cityRepository;

  @Inject
  public TranslationController(
      DictionaryService dictionaryService,
      MetaTranslationRepository metaTranslationRepository,
      CountryRepository countryRepository,
      CityRepository cityRepository) {
    this.dictionaryService = dictionaryService;
    this.metaTranslationRepository = metaTranslationRepository;
    this.countryRepository = countryRepository;
    this.cityRepository = cityRepository;
  }

  @Transactional
  public void makeTranslation(ActionRequest request, ActionResponse response) {
    List<MetaTranslation> metaTranslations = fetchUntranslatedMetaTranslations();
    if (metaTranslations == null || metaTranslations.isEmpty()) {
      logger.info("No untranslated MetaTranslations found.");
      return;
    }
    for (MetaTranslation metaTranslation : metaTranslations) {
      translateAndSaveMetaTranslation(metaTranslation);
    }
  }

  private List<MetaTranslation> fetchUntranslatedMetaTranslations() {
    return metaTranslationRepository
        .all()
        .filter("self.message IS NULL OR self.message = ''")
        .fetch();
  }

  private void translateAndSaveMetaTranslation(MetaTranslation metaTranslation) {
    try {
      String translatedText =
          dictionaryService.getTranslation(
              "en", metaTranslation.getLanguage(), metaTranslation.getKey());
      metaTranslation.setMessage(translatedText);
      metaTranslationRepository.save(metaTranslation);
      logger.info("Translated and saved MetaTranslation: {}", metaTranslation);
    } catch (Exception e) {
      logger.error("Error translating MetaTranslation with key: " + metaTranslation.getKey(), e);
    }
  }
}
