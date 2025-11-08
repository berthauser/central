package com.visus.central.ui.component;

import com.vaadin.flow.component.ComponentEvent;

public abstract class FormEvent extends ComponentEvent<AbstractForm<?>>  {

	private static final long serialVersionUID = 1L;
	private final Object entity;

	protected FormEvent(AbstractForm<?> source, Object entity) {
		super(source, false);
		this.entity = entity;
	}

	public Object getEntity() {
		return entity;
	}

}
