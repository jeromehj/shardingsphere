/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.encrypt.merge.dal;

import lombok.RequiredArgsConstructor;
import org.apache.shardingsphere.encrypt.merge.dal.impl.DecoratedEncryptColumnsMergedResult;
import org.apache.shardingsphere.encrypt.merge.dal.impl.MergedEncryptColumnsMergedResult;
import org.apache.shardingsphere.encrypt.rule.EncryptRule;
import org.apache.shardingsphere.sql.parser.binder.metadata.table.TableMetas;
import org.apache.shardingsphere.sql.parser.binder.statement.SQLStatementContext;
import org.apache.shardingsphere.sql.parser.sql.statement.SQLStatement;
import org.apache.shardingsphere.sql.parser.sql.statement.dal.dialect.mysql.DescribeStatement;
import org.apache.shardingsphere.sql.parser.sql.statement.dal.dialect.mysql.ShowColumnsStatement;
import org.apache.shardingsphere.underlying.executor.QueryResult;
import org.apache.shardingsphere.underlying.merge.engine.decorator.ResultDecorator;
import org.apache.shardingsphere.underlying.merge.result.MergedResult;
import org.apache.shardingsphere.underlying.merge.result.impl.transparent.TransparentMergedResult;

/**
 * DAL result decorator for encrypt.
 */
@RequiredArgsConstructor
public final class EncryptDALResultDecorator implements ResultDecorator {
    
    private final EncryptRule encryptRule;
    
    @Override
    public MergedResult decorate(final QueryResult queryResult, final SQLStatementContext sqlStatementContext, final TableMetas tableMetas) {
        return isNeedMergeEncryptColumns(sqlStatementContext.getSqlStatement())
                ? new MergedEncryptColumnsMergedResult(queryResult, sqlStatementContext, encryptRule) : new TransparentMergedResult(queryResult);
    }
    
    @Override
    public MergedResult decorate(final MergedResult mergedResult, final SQLStatementContext sqlStatementContext, final TableMetas tableMetas) {
        return isNeedMergeEncryptColumns(sqlStatementContext.getSqlStatement()) ? new DecoratedEncryptColumnsMergedResult(mergedResult, sqlStatementContext, encryptRule) : mergedResult;
    }
    
    private boolean isNeedMergeEncryptColumns(final SQLStatement sqlStatement) {
        return sqlStatement instanceof DescribeStatement || sqlStatement instanceof ShowColumnsStatement;
    }
}
