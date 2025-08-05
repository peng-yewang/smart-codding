package com.pyw.smartcoding;

import com.pyw.smartcoding.ai.AiSmartCoderService;
import com.pyw.smartcoding.ai.SmartCoder;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.service.Result;
import io.milvus.param.Constant;
import io.milvus.v2.client.MilvusClientV2;
import io.milvus.v2.common.DataType;
import io.milvus.v2.common.IndexParam;
import io.milvus.v2.service.collection.request.AddFieldReq;
import io.milvus.v2.service.collection.request.CreateCollectionReq;
import io.milvus.v2.service.collection.request.DropCollectionReq;
import io.milvus.v2.service.collection.request.GetLoadStateReq;
import io.milvus.v2.service.database.request.CreateDatabaseReq;
import io.milvus.v2.service.database.request.DropDatabaseReq;
import io.milvus.v2.service.database.response.ListDatabasesResp;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class SmartCodingApplicationTests {

    @Resource
    private SmartCoder smartCoder;

    @Resource
    private AiSmartCoderService aiSmartCoderService;

    @Resource
    MilvusClientV2 milvusClientV2;

    @Test
    void contextLoads() {
        smartCoder.chat("写一个java程序，打印1到1000的偶数");
    }

    @Test
    void testChatWithMessage() {
        UserMessage userMessage = UserMessage.from(
                TextContent.from("描述图片"),
                ImageContent.from("https://www.codefather.cn/logo.png")
        );
        smartCoder.chatWithMessage(userMessage);
    }

    @Test
    void chat(){
        String result = aiSmartCoderService.chat("你好，我是一名java开发工程师");
        System.out.println(result);
    }

    @Test
    void chatWithMemory() {
        String result = aiSmartCoderService.chat("你好，我是程序员鱼皮");
        System.out.println(result);
        result = aiSmartCoderService.chat("你好，我是谁来着？");
        System.out.println(result);
    }
    @Test
    void chatForReport() {
        String userMessage = "你好，我是程序员鱼皮，学编程两年半，请帮我制定学习报告";
        AiSmartCoderService.Report report = aiSmartCoderService.chatForReport(userMessage);
        System.out.println(report);
    }

    @Test
    void chatWithRag() {
        Result<String> result = aiSmartCoderService.chatWithRag("怎么学习 Java？有哪些常见面试题？");
        System.out.println(result.content());
        System.out.println(result.sources());
    }

    @Test
    void chatWithTools() {
        String result = aiSmartCoderService.chat("有哪些常见的计算机网络面试题？");
        System.out.println(result);
    }

    @Test
    void createDatabaseForMilvus(){
        Map<String, String> properties = new HashMap<>();
        properties.put(Constant.DATABASE_REPLICA_NUMBER, "2");
        milvusClientV2.createDatabase(CreateDatabaseReq.builder().databaseName("smart_codding2").properties(properties).build());
    }

    @Test
    void createCollection() throws InterruptedException {

        milvusClientV2.useDatabase("smart_codding");
        CreateCollectionReq.CollectionSchema schema = milvusClientV2.createSchema();
        schema.addField(AddFieldReq.builder()
                .fieldName("my_id")
                .dataType(DataType.Int64)
                .isPrimaryKey(true)
                .autoID(false)
                .build());

        schema.addField(AddFieldReq.builder()
                .fieldName("my_vector")
                .dataType(DataType.FloatVector)
                .dimension(256)
                .build());

        schema.addField(AddFieldReq.builder()
                .fieldName("my_varchar")
                .dataType(DataType.VarChar)
                .maxLength(512)
                .build());
        //创建索引
        IndexParam indexParamForIdField = IndexParam.builder()
                .fieldName("my_id")
                .indexType(IndexParam.IndexType.AUTOINDEX)
                .build();

        IndexParam indexParamForVectorField = IndexParam.builder()
                .fieldName("my_vector")
                .indexType(IndexParam.IndexType.AUTOINDEX)
                .metricType(IndexParam.MetricType.COSINE)
                .build();

        List<IndexParam> indexParams = new ArrayList<>();
        indexParams.add(indexParamForIdField);
        indexParams.add(indexParamForVectorField);

        //创建collections
        // 3.4 Create a collection with schema and index parameters
        CreateCollectionReq customizedSetupReq1 = CreateCollectionReq.builder()
                .collectionName("text_collection")
                .collectionSchema(schema)
                .indexParams(indexParams)
                .build();


        milvusClientV2.createCollection(customizedSetupReq1);

        // 3.5 Get load state of the collection
        GetLoadStateReq customSetupLoadStateReq1 = GetLoadStateReq.builder()
                .collectionName("text_collection")
                .build();

        Boolean loaded = milvusClientV2.getLoadState(customSetupLoadStateReq1);
        System.out.println(loaded);
    }

    @Test
    void dropCollectionFromMilvus(){
        DropCollectionReq dropQuickSetupParam = DropCollectionReq.builder()
                .collectionName("text_collection")
                .build();
        milvusClientV2.dropCollection(dropQuickSetupParam);
    }
    @Test
    void dropDatabaseFromMilvus(){
        milvusClientV2.dropDatabase(DropDatabaseReq.builder().databaseName("smart_codding").build());
    }

    @Test
    void testMilvus(){
        ListDatabasesResp listDatabasesResp = milvusClientV2.listDatabases();
        List<String> dbNames = listDatabasesResp.getDatabaseNames();
        System.out.println(dbNames);
    }


}
