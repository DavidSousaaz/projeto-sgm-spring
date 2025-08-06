package br.edu.ifpb.sgm.projeto_sgm.service;

import br.edu.ifpb.sgm.projeto_sgm.dto.*;
import br.edu.ifpb.sgm.projeto_sgm.model.Instituicao;
import br.edu.ifpb.sgm.projeto_sgm.model.Pessoa;
import br.edu.ifpb.sgm.projeto_sgm.model.Role;
import br.edu.ifpb.sgm.projeto_sgm.repository.PessoaRepository;
import br.edu.ifpb.sgm.projeto_sgm.repository.RoleRepository;
import br.edu.ifpb.sgm.projeto_sgm.util.Constants;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class TestService {

    // Injeções necessárias
    private final RoleRepository roleRepository;
    private final PessoaRepository pessoaRepository; // Usado para criar usuários especiais
    private final PasswordEncoder passwordEncoder;  // Usado para criar usuários especiais
    private final InstituicaoService instituicaoService;
    private final AlunoService alunoService;
    private final ProfessorService professorService;
    private final CursoService cursoService;
    private final DisciplinaService disciplinaService;
    private final ProcessoSeletivoService processoSeletivoService;
    private final MonitoriaService monitoriaService;
    private final AtividadeService atividadeService;

    public TestService(RoleRepository roleRepository, PessoaRepository pessoaRepository, PasswordEncoder passwordEncoder, InstituicaoService instituicaoService, AlunoService alunoService, ProfessorService professorService, CursoService cursoService, DisciplinaService disciplinaService, ProcessoSeletivoService processoSeletivoService, MonitoriaService monitoriaService, AtividadeService atividadeService) {
        this.roleRepository = roleRepository;
        this.pessoaRepository = pessoaRepository;
        this.passwordEncoder = passwordEncoder;
        this.instituicaoService = instituicaoService;
        this.alunoService = alunoService;
        this.professorService = professorService;
        this.cursoService = cursoService;
        this.disciplinaService = disciplinaService;
        this.processoSeletivoService = processoSeletivoService;
        this.monitoriaService = monitoriaService;
        this.atividadeService = atividadeService;
    }

    @PostConstruct
    private void initRoles() {
        if (roleRepository.findByRole("ROLE_" + Constants.ADMIN).isEmpty()) {
            roleRepository.save(new Role(null, "ROLE_" + Constants.ADMIN));
        }
        if (roleRepository.findByRole("ROLE_" + Constants.COORDENADOR).isEmpty()) {
            roleRepository.save(new Role(null, "ROLE_" + Constants.COORDENADOR));
        }
        if (roleRepository.findByRole("ROLE_" + Constants.DOCENTE).isEmpty()) {
            roleRepository.save(new Role(null, "ROLE_" + Constants.DOCENTE));
        }
        if (roleRepository.findByRole("ROLE_" + Constants.DISCENTE).isEmpty()) {
            roleRepository.save(new Role(null, "ROLE_" + Constants.DISCENTE));
        }
    }

    @Transactional
    public void insertTestData() {
        if (instituicaoService.findAll().isEmpty()) {
            // 1. Criar Instituição via serviço
            InstituicaoRequestDTO instDto = new InstituicaoRequestDTO();
            instDto.setNome("Instituição Teste");
            instDto.setCnpj("12.345.678/0001-99");
            instDto.setEmail("contato@instituicaoteste.com");
            InstituicaoResponseDTO instituicao = instituicaoService.save(instDto);

            // 2. Criar Admin DIRETAMENTE para garantir a Role
            Pessoa admin = new Pessoa();
            admin.setNome("Admin SGM");
            admin.setCpf("000.000.000-00");
            admin.setEmail("admin@sgm.com");
            admin.setEmailAcademico("admin.academico@sgm.com");
            admin.setMatricula("admin");
            admin.setSenha(passwordEncoder.encode("admin123"));
            admin.setInstituicao(new Instituicao(instituicao.getId(), null, null, null));
            admin.setRoles(List.of(roleRepository.findByRole("ROLE_" + Constants.ADMIN).get()));
            pessoaRepository.save(admin);

            // 3. Criar Coordenador DIRETAMENTE para garantir a Role
            Pessoa coordenador = new Pessoa();
            coordenador.setNome("Coordenador Teste");
            coordenador.setCpf("111.111.111-11");
            coordenador.setEmail("coordenador@sgm.com");
            coordenador.setEmailAcademico("coordenador.academico@sgm.com");
            coordenador.setMatricula("coordenador");
            coordenador.setSenha(passwordEncoder.encode("coord123"));
            coordenador.setInstituicao(new Instituicao(instituicao.getId(), null, null, null));
            coordenador.setRoles(List.of(roleRepository.findByRole("ROLE_" + Constants.COORDENADOR).get()));
            pessoaRepository.save(coordenador);

            // 4. Criar Curso via serviço
            CursoRequestDTO cursoDto = new CursoRequestDTO();
            cursoDto.setNome("Curso Teste");
            cursoDto.setDuracao(4);
            cursoDto.setInstituicaoId(instituicao.getId());
            cursoDto.setNivelString("GRADUACAO");
            CursoResponseDTO curso = cursoService.save(cursoDto);

            // 5. Criar Disciplina via serviço
            DisciplinaRequestDTO disciplinaDto = new DisciplinaRequestDTO();
            disciplinaDto.setNome("Disciplina Teste");
            disciplinaDto.setCargaHoraria(60);
            disciplinaDto.setCursoId(curso.getId());
            DisciplinaResponseDTO disciplina = disciplinaService.save(disciplinaDto);

            // 6. Criar Professor via serviço
            ProfessorRequestDTO profDto = new ProfessorRequestDTO();
            profDto.setNome("Professor Teste");
            profDto.setEmail("professor@instituicaoteste.com");
            profDto.setEmailAcademico("professorAcademico@instituicaoteste.com");
            profDto.setCpf("123.456.789-00");
            profDto.setMatricula("12374");
            profDto.setSenha("senha");
            profDto.setInstituicaoId(instituicao.getId());
            profDto.setDisciplinasId(List.of(disciplina.getId()));
            profDto.setCursosId(Set.of(curso.getId()));
            ProfessorResponseDTO professor = professorService.save(profDto);

            // 7. Criar Aluno via serviço
            AlunoRequestDTO alunoDTO = new AlunoRequestDTO();
            alunoDTO.setNome("Joca Teste");
            alunoDTO.setCpf("222.222.222-22");
            alunoDTO.setEmail("joca@gmail.com");
            alunoDTO.setEmailAcademico("joca.academico@gmail.com");
            alunoDTO.setMatricula("123456");
            alunoDTO.setSenha("senhaAluno");
            alunoDTO.setInstituicaoId(instituicao.getId());
            alunoService.save(alunoDTO);

            // O resto segue o mesmo fluxo...
            ProcessoSeletivoRequestDTO psDto = new ProcessoSeletivoRequestDTO();
            psDto.setInstituicaoId(instituicao.getId());
            psDto.setNumero("PS001");
            psDto.setInicio(LocalDate.now());
            psDto.setFim(LocalDate.now().plusMonths(2));
            ProcessoSeletivoResponseDTO processoSeletivo = processoSeletivoService.save(psDto);

            MonitoriaRequestDTO monitoriaDto = new MonitoriaRequestDTO();
            monitoriaDto.setDisciplinaId(disciplina.getId());
            monitoriaDto.setProfessorId(professor.getId());
            monitoriaDto.setNumeroVaga(10);
            monitoriaDto.setNumeroVagaBolsa(2);
            monitoriaDto.setCargaHoraria(4);
            monitoriaDto.setProcessoSeletivoId(processoSeletivo.getId());
            monitoriaDto.setInscricoesId(Collections.emptyList());
            MonitoriaResponseDTO monitoria = monitoriaService.save(monitoriaDto);

            AtividadeRequestDTO atividadeDto = new AtividadeRequestDTO();
            atividadeDto.setDataHora(LocalDateTime.now());
            atividadeDto.setDescricao("Atividade teste de nivelamento");
            atividadeDto.setMonitoriaId(monitoria.getId());
            atividadeService.save(atividadeDto);
        }
    }
}