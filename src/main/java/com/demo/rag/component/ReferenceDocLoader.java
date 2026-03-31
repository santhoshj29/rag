package com.demo.rag.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class ReferenceDocLoader {
    

    private static final Logger logger = LoggerFactory.getLogger(ReferenceDocLoader.class);
    private final JdbcClient jdbcClient;
    private final VectorStore vectorStore;

    @Value("classpath:/docs/spring-boot-reference.pdf")
    private Resource pdfResource;

    @Autowired
    public ReferenceDocLoader(JdbcClient jdbcClient, VectorStore vectorStore){
        this.jdbcClient = jdbcClient;
        this.vectorStore = vectorStore;
    }


    @PostConstruct
    public void init(){

        Integer count = jdbcClient.sql("select count(*) from vector_store")
                        .query(Integer.class)
                        .single();

        logger.info("Current vector_store count: {}", count);

        if(count==0){
            logger.info("Loading Spring boot reference document from resource");

            var config = PdfDocumentReaderConfig.builder()
                            .withPageExtractedTextFormatter(new ExtractedTextFormatter.Builder().withNumberOfBottomTextLinesToDelete(0)
                            .withNumberOfTopPagesToSkipBeforeDelete(0)
                            .build())
                            .withPagesPerDocument(1)
                            .build();
            

            var PdfReader = new PagePdfDocumentReader(pdfResource, config);
            var textSplitter = new TokenTextSplitter();
            vectorStore.accept(textSplitter.apply(PdfReader.get()));


            logger.info("Application is ready");
        }

    }
}
