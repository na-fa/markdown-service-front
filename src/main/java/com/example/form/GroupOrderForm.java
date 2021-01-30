package com.example.form;

import javax.validation.GroupSequence;
import com.example.form.ValidGroupForm.*;

@GroupSequence({ First.class, Second.class, Third.class })
public interface GroupOrderForm {
}