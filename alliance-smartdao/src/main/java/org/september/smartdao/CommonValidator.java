package org.september.smartdao;

import java.lang.reflect.Field;

import org.september.core.exception.BusinessException;
import org.september.smartdao.util.ReflectHelper;
import org.september.smartdao.util.SqlHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

@Component
public class CommonValidator {
	
	@Autowired
	private CommonDao commonDao;
	
    /**
     * 判断数据是否存在
     * @author yexinzhou
     * @date 2017年6月22日 上午11:15:16
     * @param clazz
     * @param fields
     * @param values
     * @return
     */
    public boolean exsits(Class<?> clazz , String[] fields , Object[] values){
        Object obj;
        try {
            obj = clazz.newInstance();
            for(int i=0;i<fields.length;i++){
            	Field field = ReflectionUtils.findField(clazz, fields[i]);
            	field.setAccessible(true);
            	ReflectionUtils.setField(field, obj, values[i]);
            }
            Object po = commonDao.getByExample(obj);
            if(po==null){
                return false;
            }else{
                return true;
            }
        }catch (Exception e) {
            throw new BusinessException("数据唯一性验证失败",e);
        }
        
    }
    
    public boolean exsitsNotMe(Class<?> clazz , String[] fields , Object[] values, Object myId){
    	Object obj;
        try {
            obj = clazz.newInstance();
            for(int i=0;i<fields.length;i++){
            	Field field = ReflectionUtils.findField(clazz, fields[i]);
            	field.setAccessible(true);
            	ReflectionUtils.setField(field, obj, values[i]);
            }
            Object po = commonDao.getByExample(obj);
            if(po==null){
            	return false;
            }
            Field idField = SqlHelper.getIdOfClass(clazz);
            Object poIdValue = ReflectionUtils.getField(idField, po);
            if(!poIdValue.equals(myId)){
            	return true;
            }else{
            	return false;
            }
        }catch (Exception e) {
            throw new BusinessException("数据唯一性验证失败",e);
        }
    }
}
