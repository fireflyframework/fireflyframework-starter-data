/*
 * Copyright 2024-2026 Firefly Software Solutions Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.fireflyframework.data.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.fireflyframework.cache.core.CacheAdapter;
import org.fireflyframework.data.cache.EnrichmentCacheKeyGenerator;
import org.fireflyframework.data.cache.EnrichmentCacheService;
import org.fireflyframework.data.cache.OperationCacheService;
import org.fireflyframework.data.service.DataEnricher;
import org.fireflyframework.data.service.DataEnricherRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

/**
 * Auto-configuration for data enrichment components.
 *
 * <p>This configuration automatically sets up:</p>
 * <ul>
 *   <li>Data enrichment properties</li>
 *   <li>Data enricher registry for discovering enrichers</li>
 *   <li>Enrichment cache service (when cache is enabled and CacheAdapter is available)</li>
 *   <li>Cache key generator for tenant-isolated caching</li>
 * </ul>
 *
 * <p>Note: EnrichmentEventPublisher is auto-discovered via @Service annotation
 * and is conditionally created based on firefly.data.enrichment.publish-events property.</p>
 *
 * <p>The configuration is activated when:</p>
 * <ul>
 *   <li>The property {@code firefly.data.enrichment.enabled} is true (default)</li>
 *   <li>Or the property is not set (enabled by default)</li>
 * </ul>
 *
 * <p><b>Example Configuration:</b></p>
 * <pre>{@code
 * firefly:
 *   data:
 *     enrichment:
 *       enabled: true
 *       publish-events: true
 *       cache-enabled: true
 *       cache-ttl-seconds: 3600
 *       default-timeout-seconds: 30
 *       max-batch-size: 100
 *       batch-parallelism: 10
 * }</pre>
 */
@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(DataEnrichmentProperties.class)
@ConditionalOnProperty(
    prefix = "firefly.data.enrichment",
    name = "enabled",
    havingValue = "true",
    matchIfMissing = true
)
@ComponentScan(basePackages = "org.fireflyframework.data")
public class DataEnrichmentAutoConfiguration {
    
    public DataEnrichmentAutoConfiguration() {
        log.info("Initializing Data Enrichment Auto-Configuration");
    }
    
    /**
     * Creates the data enricher registry bean.
     *
     * <p>This registry automatically discovers all DataEnricher beans
     * and provides methods to look them up by provider name or enrichment type.</p>
     *
     * <p>Spring will inject all DataEnricher beans into the List parameter.</p>
     */
    @Bean
    @ConditionalOnProperty(
        prefix = "firefly.data.enrichment",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
    )
    public DataEnricherRegistry dataEnricherRegistry(List<DataEnricher<?, ?, ?>> enrichers) {
        log.info("Creating DataEnricherRegistry bean with {} enrichers", enrichers.size());
        return new DataEnricherRegistry(enrichers);
    }

    /**
     * Creates the enrichment cache key generator bean.
     *
     * <p>This generator creates tenant-isolated cache keys for enrichment requests.</p>
     */
    @Bean
    public EnrichmentCacheKeyGenerator enrichmentCacheKeyGenerator(ObjectMapper objectMapper) {
        log.info("Creating EnrichmentCacheKeyGenerator bean");
        return new EnrichmentCacheKeyGenerator(objectMapper);
    }

    /**
     * Creates the enrichment cache service bean.
     *
     * <p>This service is only created when:</p>
     * <ul>
     *   <li>Cache is enabled via firefly.data.enrichment.cache-enabled=true</li>
     *   <li>A CacheAdapter bean is available (from fireflyframework-cache)</li>
     * </ul>
     *
     * <p>The cache service provides tenant-isolated caching of enrichment results.</p>
     */
    @Bean
    @ConditionalOnProperty(
        prefix = "firefly.data.enrichment",
        name = "cache-enabled",
        havingValue = "true"
    )
    @ConditionalOnBean(org.fireflyframework.cache.manager.FireflyCacheManager.class)
    public EnrichmentCacheService enrichmentCacheService(
            org.fireflyframework.cache.manager.FireflyCacheManager cacheManager,
            EnrichmentCacheKeyGenerator keyGenerator,
            DataEnrichmentProperties properties) {
        log.info("Creating EnrichmentCacheService bean with cache type: {}", cacheManager.getCacheType());
        return new EnrichmentCacheService(cacheManager, keyGenerator, properties);
    }

    /**
     * Creates the operation cache service bean.
     *
     * <p>This service is only created when:</p>
     * <ul>
     *   <li>Operation cache is enabled via firefly.data.enrichment.operations.cache-enabled=true</li>
     *   <li>A CacheAdapter bean is available (from fireflyframework-cache)</li>
     * </ul>
     *
     * <p>The cache service provides tenant-isolated caching of provider operation results.</p>
     */
    @Bean
    @ConditionalOnProperty(
        prefix = "firefly.data.enrichment.operations",
        name = "cache-enabled",
        havingValue = "true",
        matchIfMissing = true
    )
    @ConditionalOnBean(org.fireflyframework.cache.manager.FireflyCacheManager.class)
    public OperationCacheService operationCacheService(
            org.fireflyframework.cache.manager.FireflyCacheManager cacheManager,
            ObjectMapper objectMapper,
            DataEnrichmentProperties properties) {
        log.info("Creating OperationCacheService bean with cache type: {}", cacheManager.getCacheType());
        return new OperationCacheService(cacheManager, objectMapper, properties);
    }
}
