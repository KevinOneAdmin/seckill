package com.mdiai.seckill.common.validator;

import com.mdiai.seckill.common.utils.StringUtils;
import com.mdiai.seckill.common.utils.ValidatorUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Kevin.Liu
 * @Date create in 2018/7/6  18:31
 * @Description
 */
public class IsMobileValidator implements ConstraintValidator<IsMobile,String> {


    private boolean required = false;

    @Override
    public void initialize(IsMobile isMobile) {
        required = isMobile.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if(required){
            return ValidatorUtil.isMobile(value);
        }else {
            return StringUtils.isEmpty(value)?true: ValidatorUtil.isMobile(value)?true:false;
        }
    }
}
