import { listGeneratorVoByPage } from '@/services/backend/generatorController';
import { PageContainer } from '@ant-design/pro-components';
import { message } from 'antd';
import React, { useEffect, useState } from 'react';

/**
 * 默认分页参数
 */
const DEFAULT_PAGE: PageRequest = {
  current: 1,
  pageSize: 4,
  sortField: 'createTime',
  sortOrder: 'descend',
};

const IndexPage: React.FC = () => {
  //设置变量
  const [loading, setLoading] = useState<boolean>(false);
  const [dataList, setDataList] = useState<API.GeneratorVO[]>([]);
  const [total, setTotal] = useState<number>(0);
  const [searchText, setSearchText] = useState<API.GeneratorQueryRequest>({
    ...DEFAULT_PAGE,
  });
  const doSearch = async () => {
    setLoading(true);
    try {
      const res = await listGeneratorVoByPage(searchText);
      setDataList(res.data?.records ?? []);
      setTotal(Number(res.data?.total ?? 0));
    } catch (errror: any) {
      message.error('获取数据失败' + errror.message);
    }
    setLoading(false);
  };
  useEffect(() => {
    doSearch();
  }, [searchText]);

  return <PageContainer></PageContainer>;
};

export default IndexPage;
