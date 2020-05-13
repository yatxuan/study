package com.yat.graphql.model.provider;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * <p>Description: 实现的功能：将GraphQL对象载入到Spring容器，并且完成GraphQL对象的初始化的功能 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/5/9
 * @Time: 17:05
 */
@Component
public class GraphQlProvider {

    private GraphQL graphQL;

    @Autowired
    private List<MyDataFetcher> myDataFetchers;

    /**
     * 实现对GraphQL对象的初始化
     */
    @PostConstruct
    public void init() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:graphql/YatXuan.graphql");
        this.graphQL = GraphQL.newGraphQL(buildGraphQLSchema(file)).build();

    }

    private GraphQLSchema buildGraphQLSchema(File file) {
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(file);
        return new SchemaGenerator().makeExecutableSchema(typeRegistry, buildWiring());
    }

    private RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type("YatXuanQuery", builder -> {
                            // for (MyDataFetcher myDataFetcher : myDataFetchers) {
                            //     builder.dataFetcher(myDataFetcher.fieldName(),
                            //             environment -> myDataFetcher.dataFetcher(environment));
                            // }
                            myDataFetchers.forEach(
                                    myDataFetcher -> builder.dataFetcher(
                                            myDataFetcher.fieldName(),
                                            environment -> myDataFetcher.dataFetcher(environment)
                                    )
                            );
                            return builder;
                        }
                ).build();
    }

    @Bean
    public GraphQL graphQL() {
        return this.graphQL;
    }

}
