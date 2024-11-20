package com.spring.batch.configurations;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.PathResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.spring.batch.model.Coffee;

@Configuration
public class BeanConfigurations {
	
	@Value("${file.input}")
	private String inputResource;
	
	@Bean(name = "firstBatchJob")
	Job job(JobRepository jobRepository, Step step1) {
		return 	new JobBuilder("firstBatchJob", jobRepository)
				.incrementer(new RunIdIncrementer())
				.flow(step1)
				.end()
				.build();
	}

	@Bean
	Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("step1", jobRepository)
				.<Coffee, Coffee>chunk(10, transactionManager)
				.reader(reader())
//				.processor(processor())
				.writer(writer())
				.build();
	}
	
	@Bean
	FlatFileItemWriter<Coffee> writer() {
		DelimitedLineAggregator<Coffee> delimitedLineAggregator = new DelimitedLineAggregator<>();
		return new FlatFileItemWriterBuilder<Coffee>().name("customWriter")
				.resource(new FileSystemResource("output/output.json"))
				.lineAggregator(delimitedLineAggregator) // from dto 
				// for custom delimeter
//				.delimited()  
//				.delimiter(":::")
//				.names("brand", "origin", "characteristics")
				.append(true)
				.build();
	}

	@Bean
	ItemProcessor<Coffee, Coffee> processor() {
		return p -> p;
	}

	@Bean
	FlatFileItemReader<Coffee> reader()  {
		BeanWrapperFieldSetMapper<Coffee> mapper = new BeanWrapperFieldSetMapper<>();
		mapper.setTargetType(Coffee.class);
		return new FlatFileItemReaderBuilder<Coffee>().name("customReader")
				.resource(new PathResource(inputResource))
				.delimited()
				.names("brand", "origin", "characteristics")
//				.fieldSetMapper(new CofeeFieldSetMapper()) // way 1
//				.fieldSetMapper(new BeanWrapperFieldSetMapper<Coffee>() {{setTargetType(Coffee.class);}}) // way 2
				.fieldSetMapper(mapper) // way 3
				.strict(false)
				.build();
	}
}
