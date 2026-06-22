package com.visus.central.infraestructure.util;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Service;

import com.vaadin.flow.router.Route;
import com.visus.central.domain.port.out.PermisoVistaRepository;
import com.visus.central.ui.view.HomeView;

import jakarta.annotation.PostConstruct;

@Service
public class ViewScannerService {

	private static final Logger log = LoggerFactory.getLogger(ViewScannerService.class);
	private static final String VIEW_PACKAGE = "com.visus.central.ui.view";

	private final PermisoVistaRepository permisoRepository;
	private final Set<String> excludedViews;

	public ViewScannerService(PermisoVistaRepository permisoRepository,
			@Value("${app.permissions.excluded-views:}") String excludedViewsCsv) {
		this.permisoRepository = permisoRepository;
		this.excludedViews = parseExcludedViews(excludedViewsCsv);
	}

	private static Set<String> parseExcludedViews(String csv) {
		if (csv == null || csv.isBlank())
			return Set.of();
		return Set.of(csv.split("\\s*,\\s*"));
	}

	@PostConstruct
	public void scanAndRegisterViews() {
		try {
			ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(
					false);
			scanner.addIncludeFilter(new AnnotationTypeFilter(Route.class));

			Set<Class<?>> viewClasses = new LinkedHashSet<>();

			for (BeanDefinition bd : scanner.findCandidateComponents(VIEW_PACKAGE)) {
				String className = bd.getBeanClassName();
				if (className == null)
					continue;

				if (excludedViews.contains(className)) {
					log.info("Skipping excluded view: {}", className);
					continue;
				}

				Class<?> viewClass = Class.forName(className);

				if (ViewRegistry.isPublica(viewClass) || viewClass == HomeView.class)
					continue;

				viewClasses.add(viewClass);
			}

			ViewRegistry.init(viewClasses);
			log.info("Registered {} views for permission control", viewClasses.size());

			syncStaleAndExcludedEntries(viewClasses);

		} catch (Exception e) {
			log.error("Failed to scan views for permissions", e);
		}
	}

	private void syncStaleAndExcludedEntries(Set<Class<?>> currentViews) {
		try {
			List<com.visus.central.domain.model.PermisoVista> all = permisoRepository.findAll();
			Set<String> validClassNames = new LinkedHashSet<>();
			validClassNames.add("");
			for (Class<?> clazz : currentViews) {
				validClassNames.add(clazz.getName());
			}

			for (com.visus.central.domain.model.PermisoVista p : all) {
				if (!validClassNames.contains(p.getVistaClase())) {
					permisoRepository.deleteById(p.getId());
					log.info("Deleted stale permission entry: vista_clase={}, usuarioId={}",
							p.getVistaClase(), p.getUsuarioId());
				}
			}
		} catch (Exception e) {
			log.warn("Could not sync stale permission entries: {}", e.getMessage());
		}
	}
}
