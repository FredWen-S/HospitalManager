const DEPARTMENT_ALIAS = {
  "呼吸内科": "呼吸与危重症医学科",
  "心内科": "心血管内科",
  "皮肤科": "皮肤性病科",
  "耳鼻喉科": "耳鼻咽喉科",
  "全科门诊": "急诊科"
};

export function getTargetDepartment(department) {
  if (!department) return "";
  return DEPARTMENT_ALIAS[department] || department;
}

export function buildGuidanceRegisterPath(department) {
  const targetDepartment = getTargetDepartment(department);
  if (!targetDepartment) return "/orderOperate";
  return (
    "/sectionMessage?sectionOpt=" +
    encodeURIComponent(targetDepartment) +
    "&fromGuidance=1&guidanceDepartment=" +
    encodeURIComponent(department)
  );
}

export { DEPARTMENT_ALIAS };
