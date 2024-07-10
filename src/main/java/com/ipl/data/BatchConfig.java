package com.ipl.data;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.context.annotation.Configuration;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;

import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.ipl.model.MatchTeam;

import jakarta.persistence.EntityManagerFactory;

@Configuration
public class BatchConfig {

    @Bean
    public FlatFileItemReader<MatchInput> reader() {
        return new FlatFileItemReaderBuilder<MatchInput>()
                .name("MatchInputReader")
                .resource(new ClassPathResource("match-data.csv"))
                .delimited()
                .names("id", "city", "date", "player_of_match", "venue", "neutral_venue", "team1",
                        "team2", "toss_winner", "toss_decision", "winner", "result", "result_margin", "eliminator",
                        "method", "umpire1", "umpire2")
                .targetType(MatchInput.class)
                .build();
    }

    @Bean
    public ItemProcessor<MatchInput, MatchTeam> processor() {
        return new MatchDataProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<MatchTeam> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<MatchTeam>()
                .sql("INSERT INTO match_team (id,city,date,player_of_match,venue,neutral_venue,team1,team2,toss_winner,toss_decision,winner,result,result_margin,eliminator,method,umpire1,umpire2)"
                        + " VALUES (:id,:city,:date,:player_of_match,:venue,:neutral_venue,:team1,:team2,:toss_winner,:toss_decision,:winner,:result,:result_margin,:eliminator,:method,:umpire1,:umpire2)")
                .dataSource(dataSource)
                .beanMapped()
                .build();
    }

    @Bean
    public Job importUserJob(JobRepository jobRepository, Step step1, JobExecutionListener listener) {
        return new JobBuilder("importUserJob", jobRepository)
                .listener(listener)
                .start(step1)
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager,
            FlatFileItemReader<MatchInput> reader, ItemProcessor<MatchInput, MatchTeam> processor,
            ItemWriter<MatchTeam> writer) {
        return new StepBuilder("step1", jobRepository)
                .<MatchInput, MatchTeam>chunk(3, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

//    @Bean
//    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }
    
    @Bean(name="transactionManager")
    public PlatformTransactionManager transactionManger(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}