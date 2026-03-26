package com.demo.rag.config;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;



import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;


@Configuration
public class ragconfig {

    private static final Logger logger = LoggerFactory.getLogger(ragconfig.class);

    @Value("classpath:/docs/olympics-faq.txt")
    private Resource faq;

    @Value("vectorStoreName.json")
    String vectorStoreName = "vectorStoreName";


    public File getVectorStoreFile(){
        Path path = Paths.get("src","main","resources","data");

        String absolutePath = path.toFile().getAbsolutePath() + "/" + "vectorStoreName";
        return new File(absolutePath);
    }


    @Bean
    public SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel) {
        
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();
        File vectorStoreFile = getVectorStoreFile();

        if(vectorStoreFile.exists()){
            logger.info("Vector store file exixts at path:"+ vectorStoreFile.getAbsolutePath());
            simpleVectorStore.load(vectorStoreFile);
        }
        
        else{
            logger.info("Vector store file does not exist at path:"+ vectorStoreFile.getAbsolutePath());
            TextReader textReader = new TextReader(faq);
            textReader.getCustomMetadata().put("fileName","olympics-faq.txt");
            List<Document> documents = textReader.get();
            TokenTextSplitter splitter = new TokenTextSplitter();

            List<Document> splittedDocuments = splitter.apply(documents);

            simpleVectorStore.add(splittedDocuments);

            simpleVectorStore.save(vectorStoreFile);
        }

        return simpleVectorStore;
        
    }

    

}
