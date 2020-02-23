package com.ljr.weibo.aspect;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


//@Component
//@Aspect
//@EnableAspectJAutoProxy

/**
 * 采用redis 此方法废弃
 */
@Deprecated
public class ImgCacheAspect {
    private Map<String,Object> CACHE_CONTAINER= new HashMap<>();
    //日子输出
    private Log log= LogFactory.getLog(ImgCacheAspect.class.getSimpleName());


    //log前缀
    private static final String CACHE_IMG_PREFIX="img:";

    //图片的切入点
    private static final String POINTCUT_IMG_LIST="execution(* com.ljr.weibo.service.impl.NewsServiceImpl.findImgsByNid(..))";
    private static final String POINTCUT_IMG_SAVE="execution(* com.ljr.weibo.service.impl.NewsServiceImpl.saveImgByNid(..))";
    private static final String POINTCUT_IMG_DELETE="execution(* com.ljr.weibo.service.impl.NewsServiceImpl.removeImgByNid(..))";

    //头像的切入点
    private static final String CACHE_ICON_PREFIX="icon:";
    private static final String POINTCUT_ICON_SELECT="execution(* com.ljr.weibo.service.impl.UserServiceImpl.findUserIconByUid(..))";
    private static final String POINTCUT_ICON_UPDATE="execution(* com.ljr.weibo.service.impl.UserServiceImpl.updateUserIconByUid(..))";

    /**
     * 查询对应文章nid下面的图片
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around(value = POINTCUT_IMG_LIST)
    public Object cacheImgList(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Integer newsId= (Integer) proceedingJoinPoint.getArgs()[0];
        if(null==CACHE_CONTAINER.get(CACHE_IMG_PREFIX+newsId)){
            log.info("图片未在缓存中找到---->之后加入缓存"+newsId);
            List<String> imgs = (List<String>) proceedingJoinPoint.proceed();
            CACHE_CONTAINER.put(CACHE_IMG_PREFIX+newsId,imgs);
            return imgs;
        }else {
            log.info("图片已在缓存中找到---->"+newsId);
            return CACHE_CONTAINER.get(CACHE_IMG_PREFIX+newsId);
        }

    }

    @Around(value = POINTCUT_IMG_SAVE)
    public Object cacheImgSave(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Integer newsId= (Integer) proceedingJoinPoint.getArgs()[0];
        List<String> imgs = (List<String>) proceedingJoinPoint.getArgs()[1];
        boolean isSuccess = (boolean) proceedingJoinPoint.proceed();
        if(isSuccess){
            CACHE_CONTAINER.put(CACHE_IMG_PREFIX+newsId,imgs);
            log.info("有新的图片加入缓存");
        }
        return isSuccess;
    }



    @Around(value = POINTCUT_IMG_DELETE)
    public Object cacheImgDelete(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Integer newsId= (Integer) proceedingJoinPoint.getArgs()[0];
        boolean isSuccess = (boolean) proceedingJoinPoint.proceed();
        if(isSuccess){
            CACHE_CONTAINER.remove(CACHE_IMG_PREFIX+newsId);
            log.info("删除缓存");
        }
        return isSuccess;
    }



    @Around(value = POINTCUT_ICON_SELECT)
    public Object cacheIconSelect(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Integer uid = (Integer) proceedingJoinPoint.getArgs()[0];
        String o = (String) CACHE_CONTAINER.get(CACHE_ICON_PREFIX + uid);
        if(null== o){
            String icon = (String) proceedingJoinPoint.proceed();
            CACHE_CONTAINER.put(CACHE_ICON_PREFIX+uid,icon);
            log.info("未从缓存里面找到图片，放入缓存------------>"+uid);
            return icon;
        }
        log.info("从缓存里面找到图片------------>"+uid);
        return o;
    }

    @Around(value = POINTCUT_ICON_UPDATE)
    public Object cacheIconUpdate(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Boolean isSuccess = (Boolean) proceedingJoinPoint.proceed();
        if(isSuccess){
           Integer uid= (Integer) proceedingJoinPoint.getArgs()[0];
            String icon= (String) proceedingJoinPoint.getArgs()[1];
            CACHE_CONTAINER.put(CACHE_ICON_PREFIX+uid,icon);
            log.info("头像缓存跟新------------>"+uid);
        }
        return isSuccess;
    }
}
