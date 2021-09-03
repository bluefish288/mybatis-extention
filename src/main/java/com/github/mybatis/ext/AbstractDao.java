package com.github.mybatis.ext;

import com.github.mybatis.ext.support.Context;
import com.github.mybatis.ext.model.InsertModel;
import com.github.mybatis.ext.model.UpdateModel;
import com.github.mybatis.ext.descriptor.ModelDescriptor;
import com.github.mybatis.ext.descriptor.ModelWrapper;
import com.github.mybatis.ext.page.Page;
import com.github.mybatis.ext.page.PageParam;
import com.github.mybatis.ext.descriptor.FieldDescriptor;
import com.github.mybatis.ext.descriptor.ModelDescriptorContext;
import org.apache.ibatis.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface AbstractDao {

    public final static String INSERT_METHOD_NAME = "insertByModel";

    public final static String UPDATE_METHOD_NAME = "updateByModel";

    public final static String FIND_BY_ID_METHOD = "findModelById";

    @InsertProvider(type = ModelSqlProvider.class, method = INSERT_METHOD_NAME)
    @Options(useGeneratedKeys = true, keyProperty = "model.id")
    public boolean insertByModel(InsertModel insertModel);

    @UpdateProvider(type = ModelSqlProvider.class, method = UPDATE_METHOD_NAME)
    public int updateByModel(UpdateModel updateModel);

    public default  <R, P extends PageParam> Page<R> findPage(P pageParam, Function<P, List<R>> dataListFunc){

        if(null == pageParam || null == dataListFunc){
            return Page.EMPTY_PAGE;
        }

        List<R> datas = dataListFunc.apply(pageParam);

        if(null == datas || datas.size() == 0){
            return Page.EMPTY_PAGE;
        }

        int totalCount = pageParam.isAll() ? datas.size() : Context.getPageCount();

        Page<R> page = new Page<>(pageParam, totalCount);
        page.getDataList().addAll(datas);

        Context.clear();
        return page;
    }

    @SelectProvider(type = ModelSqlProvider.class, method = FIND_BY_ID_METHOD)
    public <T> T findModelById(@Param("clazz") Class<T> clazz, @Param("id") Object id);

    class ModelSqlProvider {

        private final static Logger logger = LoggerFactory.getLogger(ModelSqlProvider.class);

        public String insertByModel(InsertModel insertModel){

            Object model = insertModel.getModel();

            ModelWrapper<?> wrapper = new ModelWrapper<>(model);

            String tableName = wrapper.tableName();

            Map<String, Object> columnValueMap = wrapper.columnValueMapWithoutPK();


            StringBuilder insertStatement = new StringBuilder();
            List<Object> values = new ArrayList<>();


            insertStatement.append("INSERT");
            if(insertModel.isIgnoreOnDuplicate()){
                insertStatement.append(" IGNORE");
            }
            insertStatement.append(" INTO ")
                    .append(tableName)
                    .append(" (");

            int columnCount = 0;
            for(String column : columnValueMap.keySet()){
                Object value = columnValueMap.get(column);
                if(null== value){
                    continue;
                }

                if(columnCount > 0){
                    insertStatement.append(", ");
                }
                insertStatement.append(column);

                values.add(value);

                columnCount ++ ;

            }

            if(columnCount == 0){
                return null;
            }

            insertStatement.append(") VALUES(");

            for (int i = 0; i < columnCount; i++) {
                if (i > 0) {
                    insertStatement.append(", ");
                }
                insertStatement.append("?");
            }
            insertStatement.append(")");

            Context.setInsertUpdateArgs(values);

            Context.setModelPKField("model."+wrapper.getPrimaryKeyFieldName());

            return insertStatement.toString();
        }

        public String updateByModel(UpdateModel updateModel){

            Object model = updateModel.getModel();

            ModelWrapper<?> wrapper = new ModelWrapper<>(model);

            String tableName = wrapper.tableName();

            String pkColumn = wrapper.getPrimaryKeyColumn();

            Object pkValue = wrapper.getPrimaryKeyValue();

            Map<String, Object> columnValueMap = wrapper.columnValueMapWithoutPK();


            StringBuilder updateStatement = new StringBuilder();
            List<Object> values = new ArrayList<>();

            updateStatement.append("UPDATE ")
                    .append(tableName)
                    .append(" SET ");

            int columnCount = 0;
            for(String column : columnValueMap.keySet()){
                Object value = columnValueMap.get(column);

                if(null == value && (!updateModel.isAllowUpdateNull())){
                    continue;
                }

                if(columnCount > 0){
                    updateStatement.append(", ");
                }
                updateStatement.append(column).append(" = ?");

                values.add(value);

                columnCount ++ ;

            }

            updateStatement.append(" where "+pkColumn+" = ?");
            values.add(pkValue);

            Context.setInsertUpdateArgs(values);

            return updateStatement.toString();
        }

        public String findModelById(Map<String,Object> map){
            Class<?> clazz = (Class<?>) map.get("clazz");
            Object id = map.get("id");
            ModelDescriptor<?> descriptor = ModelDescriptorContext.INSTANCE.get(clazz);
            if(null == descriptor){
                return null;
            }

            StringBuilder columns = new StringBuilder();

            for (FieldDescriptor fd : descriptor.fieldDescriptors()) {
                if (columns.length() > 0) {
                    columns.append(", ");
                }
                columns.append(fd.getColumn());
            }

            String sql = "select "+columns.toString()+" from " + descriptor.tableName() + " where "+descriptor.getPrimaryKeyColumn()+" = "+id;

            logger.debug(sql);

            return sql;
        }



    }
}