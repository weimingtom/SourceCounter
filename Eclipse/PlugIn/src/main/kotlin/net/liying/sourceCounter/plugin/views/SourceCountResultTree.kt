package net.liying.sourceCounter.plugin.views

import org.eclipse.swt.*
import org.eclipse.swt.widgets.*

import org.eclipse.core.resources.*

import org.eclipse.wb.swt.ResourceManager

import net.liying.sourceCounter.plugin.FileCountResult

import net.liying.sourceCounter.plugin.views.component.BaseSourceCountResultTree

class SourceCountResultTree(parent: Composite, style: Int): BaseSourceCountResultTree(parent, style) {
	private var resultList = mutableListOf<FileCountResult>()

	private val treeItemMap = mutableMapOf<IContainer, TreeItem>()

	private val imageProject = ResourceManager.getPluginImageDescriptor(
			"SourceCounter", "images/icons/project.gif").createImage()

	private val imageFolder = ResourceManager.getPluginImageDescriptor(
			"SourceCounter", "images/icons/folder.gif").createImage()

	fun showResult(resultList: List<FileCountResult>) {
		this.resultList = resultList.toMutableList()

		this.resultList.sortBy {
			result -> result.file.fullPath.toString()
		}

		this.displayResultTree()
	}

	// =========================================================================

	private fun displayResultTree() {
		this.treeItemMap.clear()

		this.tree.removeAll()

		this.resultList.forEach {
			result -> this.createTreeItem(result.file.getParent())
		}
	}

	private fun createTreeItem(container: IContainer): TreeItem {
		var treeItem = this.treeItemMap.get(container)
		if (treeItem != null) {
			return treeItem
		}

		val parent = container.parent
		treeItem =
			if (parent != null && !(parent is IWorkspaceRoot)) {
				val parentItem = this.createTreeItem(parent)
				TreeItem(parentItem, SWT.NONE)
			} else {
				TreeItem(this.tree, SWT.NONE)
			}

		treeItem.data = container

		treeItem.setText(container.name)

		when (container) {
			is IProject -> treeItem.setImage(0, this.imageProject)
			is IFolder -> treeItem.setImage(0, this.imageFolder)
		}

		this.treeItemMap.put(container, treeItem)

		return treeItem
	}

}
