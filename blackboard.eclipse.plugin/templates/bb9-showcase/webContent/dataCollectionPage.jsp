<%@ taglib prefix="bbNG" uri="/bbNG"%>

<bbNG:learningSystemPage ctxId="ctx">
	<bbNG:pageHeader>
		<bbNG:pageTitleBar
			title="An example of a page using &lt;bbNG:dataCollection&gt;">
		</bbNG:pageTitleBar>
		<bbNG:breadcrumbBar>
			<bbNG:breadcrumb>Data Collection Page</bbNG:breadcrumb>
		</bbNG:breadcrumbBar>
	</bbNG:pageHeader>

	<bbNG:dataCollection>
		<bbNG:step title="Step One">
			<bbNG:dataElement label="A Text Input" isRequired="true">
				<bbNG:textElement name="testTextInput"
					helpText="This is an example of a text input." />
			</bbNG:dataElement>
			<bbNG:dataElement isSubElement="true" label="A Parent Element">
				<bbNG:dataElement isSubElement="true" label="A Sub Element"
					isRequired="true" labelFor="checkBoxExample1">
					<bbNG:textElement name="textInputExample" id="textInputExample"
						value="Enter Text Here" />
				</bbNG:dataElement>
				<bbNG:dataElement isSubElement="true" label="A Sub Element">
					<bbNG:checkboxElement name="checkboxExample" value="cb2" />
				</bbNG:dataElement>
				<bbNG:dataElement isSubElement="true" label="A Sub Element">
					<bbNG:checkboxElement name="checkboxExample" value="cb3" />
				</bbNG:dataElement>
				<bbNG:dataElement isSubElement="true" label="A Sub Element">
					<bbNG:checkboxElement name="checkboxExample" value="cb4" />
				</bbNG:dataElement>
			</bbNG:dataElement>
			<bbNG:dataElement label="Context Menu Example">
				<bbNG:delegateContextMenu>
					<bbNG:contextMenuItem url="#" title="Example Menu Item" />
				</bbNG:delegateContextMenu>
			</bbNG:dataElement>
		</bbNG:step>
		<bbNG:step title="Step Two">
			<bbNG:dataElement label="Multi Select Example">
				<bbNG:multiSelect formName="exampleMulti"
					sourceCollection="${r"${r"${actionBean.multiSelectOptions}"}"}">
				</bbNG:multiSelect>
			</bbNG:dataElement>
			<bbNG:dataElement label="Date Picker Example">
				<bbNG:datePicker helpText="This is a blackboard datePicker widget." />
			</bbNG:dataElement>
			<bbNG:dataElement label="Date Range Picker Example">
				<bbNG:dateRangePicker
					helpText="This is a blackboard dateRangePicker widget." />
			</bbNG:dataElement>
			<bbNG:dataElement label="Color Picker Example">
				<bbNG:colorPicker name="colorPickerExample" />
			</bbNG:dataElement>
			<bbNG:dataElement>
				<bbNG:filePicker baseElementName="filePickerExample" var="fileName"></bbNG:filePicker>
			</bbNG:dataElement>
			<bbNG:dataElement>
			</bbNG:dataElement>
			<bbNG:hierarchyList >
				<bbNG:hierarchyListItem title="Item number One">A description for item number One</bbNG:hierarchyListItem>
				<bbNG:hierarchyListItem title="Item Number Two">A description for item number Two</bbNG:hierarchyListItem>
				<bbNG:hierarchyListItem title="Item Number Three">A description for item number Three</bbNG:hierarchyListItem>
			</bbNG:hierarchyList>
			
			
		</bbNG:step>

		<bbNG:stepSubmit>
		</bbNG:stepSubmit>
	</bbNG:dataCollection>
</bbNG:learningSystemPage>
