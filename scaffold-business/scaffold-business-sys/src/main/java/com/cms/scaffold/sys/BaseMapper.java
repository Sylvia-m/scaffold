/**  
 * @Title: BaseMapper.java
 * @Package com.cms.scaffold.common.mapper
 * TODO
 * @author zhangjiahengpoping@gmail.com
 * @date 2017-6-27
 */
package com.cms.scaffold.sys;

import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;


/**
 * mapper的基类
 * 
 * @author zhangjiahengpoping@gmail.com
 * @date 2017-6-27
 */
public interface BaseMapper<T> {


	/**
	 * 通过ID查询
	 * 
	 * @param id
	 * @return
	 */
	T selectById(@Param("id") Serializable id);

	
	/**
	 * 通过ID查询
	 * 
	 * @param id
	 * @return
	 */
	T selectLockById(Serializable id);

	/**
	 * 查询单条记录
	 * 
	 * @param record
	 * @return
	 */
	T selectOne(T record);

	/**
	 * 查询记录集合
	 * 
	 * @param record
	 * @return
	 */
	List<T> findList(T record);
	
	int insert(@Param("item") T record);

	/**
	 * 批量保存
	 * 
	 * @param list
	 */
	int batchInsert(List<?> list);

	/**
	 * 通用的修改方法
	 * 
	 * @param record
	 */
	int update(@Param("item") T record);

	/**
	 * 批量更新
	 *
	 * @param list
	 * @return
	 */
	int batchUpdate(@Param("list") List<?> list);

	/**
	 * 批量更新
	 *
	 * @param list
	 * @return
	 */
	int batchUpdate(@Param("list") List<?> list, @Param("oldList") List<?> oldList);

	/**
	 * 通用的全部修改方法
	 *
	 * @param record
	 */
	int updateAll(@Param("item") T record);

	/**
	 * 通用的修改比较方法
	 * @param recordNew 新对象
	 * @param recordOld 老对象
	 * @return
	 */
	int updateCompare(@Param("item") T recordNew,@Param("itemOld") T recordOld);
	/**
	 * 批量删除
	 * 
	 * @param list
	 * @return
	 */
	int delList(List<?> list);

	/**
	 * 批量删除方法
	 * 
	 * @param ids
	 */
	int delArray(Long[] ids);

	/**
	 * 统计查询
	 * 
	 * @param record
	 *            查询参数
	 * @return 总记录条数
	 */
	int count(T record);

}
